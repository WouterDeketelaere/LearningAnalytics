$.fn.enterKey = function (fnc) {
    return this.each(function () {
        $(this).keypress(function (ev) {
            var keycode = (ev.keyCode ? ev.keyCode : ev.which);
            if (keycode == '13') {
                fnc.call(this, ev);
            }
        })
    })
}

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

function executeProcess(url, message) {
    var bars = document.getElementsByClassName("progress-bar");
    bars[0].style.width = "0%";
    stompClient.send(url, {}, JSON.stringify(message));
}

function updateProcessName(process, progress) {
    var name = document.getElementById("process-name");
    name.innerHTML = process + " " + parseFloat(Math.round(progress)).toFixed(0) + "%"
}

function updateProgressBar(progressvalue) {
    var bars = document.getElementsByClassName("progress-bar");
    bars[0].style.width = progressvalue + "%";
    $("sr-only")[0];
}

$(function () {
    // connect to Web Socket for process progress updates
    connect();

    $("#roc_form_1").on('submit', function (e) {
        e.preventDefault();
    });

    $("#roc_form_2").on('submit', function (e) {
        e.preventDefault();
    });

    $("#predict_form").on('submit', function (e) {
        // e.preventDefault();
    });

    $("#predict_studentids").enterKey(function () {
        var url = [];
        var studentids = this.value;
        var filtercode = $("#fc_slider_predict").val();
        var mapper = $("#outcome_slider").val();
        var instrument = $("#instrument").val();
        url.push('/dashboard/predict', '?', 'filtercode=', filtercode, '&mapper=', mapper, "&instrument=", instrument, "&studentids=", studentids);
        plot_prediction_chart(url.join(""));
    });

    $("#avsbc").click(function () {
        plot_roc_chart("/dashboard/roc?which=avsbc", "#roc1", "A vs BC")
    });

    $("#cvsab").click(function () {
        plot_roc_chart("/dashboard/roc?which=cvsab", "#roc2", "C vs AB")
    });

    $("#roc1slider").on('slideStop', function (e) {
        plot_sankey("/dashboard/tracks?which=avsbc&threshold=" + e.value);
    });

    $("#roc2slider").on('slideStop', function (e) {
        plot_sankey("/dashboard/tracks?which=cvsab&threshold=" + e.value);
    });

    $('#weights_btn').on('click', function (e) {
        var statistic_type = $('#statistic').val();
        var dataset = $('#dataset').val();
        var filtercode = $('#fc_slider_attribute').val();
        plot_attribute_weights("/dashboard/weights?statistic=" + statistic_type + "&filtercode=" + filtercode + "&input=" + dataset);
    });
    plot_sankey("/dashboard/tracks?which=avsbc");
});

