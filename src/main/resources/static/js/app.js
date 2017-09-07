var stompClient = null;

function connect() {
    var socket = new SockJS('/progress-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({},
        function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/queue/progressvalues', function (data) {
                var progress = JSON.parse(data.body).progressValue;
                var process = JSON.parse(data.body).processName;
                updateProgressBar(progress);
                updateProcessName(process, progress);
            });
        },
        function (error) {
            alert(error.headers.message);
        }
    );
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function updateProcessName(process, progress) {
    var name = document.getElementById("process-name");
    name.innerHTML = process + " " + roundValue(progress) + "%"
}

function updateProgressBar(progressvalue) {
    var bars = document.getElementsByClassName("progress-bar");
    bars[0].style.width = progressvalue + "%";
    $("sr-only")[0];
}

function roundValue(value, exp) {
    if (typeof exp === 'undefined' || +exp === 0)
        return Math.round(value);

    value = +value;
    exp = +exp;

    if (isNaN(value) || !(typeof exp === 'number' && exp % 1 === 0))
        return NaN;

    // Shift
    value = value.toString().split('e');
    value = Math.round(+(value[0] + 'e' + (value[1] ? (+value[1] + exp) : exp)));

    // Shift back
    value = value.toString().split('e');
    return +(value[0] + 'e' + (value[1] ? (+value[1] - exp) : -exp));
}

function initbuttons() {
    $($('.fc_pred')[0]).bootstrapToggle('on');

    $($('.fc_attr')[0]).bootstrapToggle('on');

    $($('.mapper')[0]).bootstrapToggle('on');

    $('#fpr1_slider').slider('setValue',0.16);
    $('#fpr2_slider').slider('setValue',0.13);
}

function extractFiltercodes(clazz) {
    var codes = ['11', '12', '13', '21', '22', '23', '31', '32', '33'];
    var filtercodes = "";
    $(clazz).each(function (i) {
        if (this.checked) {
            filtercodes += codes[i] + ",";
        }
    });
    return filtercodes.slice(0, -1);
}

function extractMapper() {
    var mapper = 0;
    $('.mapper').each(function (i) {
        if (this.checked) {
            mapper = i;
        }
    });
    return mapper + 1;
}


$.fn.enterKey = function (fnc) {
    return this.each(function () {
        $(this).keypress(function (ev) {
            var keycode = (ev.keyCode ? ev.keyCode : ev.which);
            if (keycode == '13') {
                fnc.call(this, ev);
            }
        })
    })
};

$(function () {
    $('.chosen').chosen();
    // connect to Web Socket for process progress updates
    connect();
    // toggle default filter code buttons
    initbuttons();
    // on load plot sankey plot of tracks
    plot_sankey("/dashboard/tracks?which=avsbc");

    // eventhandler for predict button
    $("#predict_btn").click(function () {
        var studentids = $('#predict_studentids').val();
        var params = {
            filtercode: extractFiltercodes('.fc_pred'),
            mapper: extractMapper(),
            statistic: $('#pred_statistic').val(),
            instrument: $("#instrument").val(),
            comprehensive: $('#predict_comprehensive').prop("checked"),
            studentids: studentids
        };
        var url = '/dashboard/predict?';
        plot_prediction_chart(url + $.param(params));
    });

    $("#avsbc").click(function () {
        plot_roc_chart("/dashboard/roc?which=avsbc", "#roc1", "A vs BC")
    });

    $("#cvsab").click(function () {
        plot_roc_chart("/dashboard/roc?which=cvsab", "#roc2", "C vs AB")
    });

    $("#fpr1_slider").on('slideStop', function (e) {
        plot_sankey("/dashboard/tracks?which=avsbc&threshold=" + e.value);
    });

    $("#fpr2_slider").on('slideStop', function (e) {
        plot_sankey("/dashboard/tracks?which=cvsab&threshold=" + e.value);
    });

    $('#weights_btn').on('click', function (e) {
        var params = {
            statistic: $('#attr_statistic').val(),
            input: $('#input').val(),
            filtercode: extractFiltercodes('.fc_attr'),
            comprehensive: $('#attr_comprehensive').prop("checked")
        };
        var url = "/dashboard/weights?";
        plot_attribute_weights(url + $.param(params));
    });
    // eventhandler for cluster create button.
    // Clusters will be created and stored on backend after RM process has ran.
    $('#cluster_btn').on('click', function (e) {
        var params = {
            input: $('#clusterinput').val()
        };

        $.ajax({
            url: "/dashboard/createclusters?" + $.param(params)
        });
    });
    // eventhandler that select & displays a backend stored cluster
    $('#cluster_select').on('change', function(e){
        var params1 = {
            type: 0
        };
        var params2 = {
            type: 1
        };
        var url = "/dashboard/getclusters?";
        plotRadarPlot("#radarchart1",url + $.param(params1),parseInt(this.value));
        plotRadarPlot("#radarchart2",url + $.param(params2),parseInt(this.value));
    });

});

