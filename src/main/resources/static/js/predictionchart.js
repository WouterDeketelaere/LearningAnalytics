//var slider_predict = new Slider("#fc_slider_predict", {
$("#fc_slider_predict").slider({
    ticks: [1, 2, 3, 4, 5, 6, 7, 8, 9],
    ticks_labels: ["11", "12", "13", "21", "22", "23", "31", "32", "33"],
    ticks_snap_bounds: 60,
    value: 1
});

$("#outcome_slider").slider({
    ticks: [1, 2, 3, 4, 5],
    ticks_labels: ["All", "Two", "Three", "Four", "Five"],
    ticks_snap_bounds: 60,
    value: 1
});

function plot_prediction_chart(url) {
    // set the dimensions and margins of the graph
    var $container = $('#predictionchart'), width = $container.width();

    var margin = {top: 20, right: 20, bottom: 30, left: 30},
        width = width - margin.left - margin.right,
        height = 400 - margin.top - margin.bottom;

    // set the ranges
    var x_pr = d3.scaleBand()
        .range([0, width])
        .padding(0.1);
    var y_pr = d3.scaleLinear()
        .range([height, 0]);

    d3.select("#predictionchart").select("svg").remove();

    var svg = d3.select("#predictionchart").append("svg")
        .attr("width", '100%')
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");


    d3.json(url, function (error, jsonData) {
        if (error) throw error;

        // format the data
        var data = formatprediction(jsonData);

        // Scale the range of the data in the domains
        x_pr.domain(data.map(function (d) {
            return d.Attribute;
        }));
        y_pr.domain([0, d3.max(data, function (d) {
            return d.Weight;
        })]);

        // append the rectangles for the bar chart
        svg.selectAll(".bar")
            .data(data)
            .enter().append("rect")
            .attr("class", "bar")
            .attr("x", function (d) {
                return x_pr(d.Attribute);
            })
            .attr("width", x_pr.bandwidth())
            .attr("y", function (d) {
                return y_pr(d.Weight);
            })
            .attr("height", function (d) {
                return height - y_pr(d.Weight);
            });

        // add the x Axis
        svg.append("g")
            .attr("transform", "translate(0," + height + ")")
            .call(d3.axisBottom(x_pr))
            .call(d3.axisBottom(x_pr))
            .selectAll("text")
            .style("text-anchor", "end")
            .attr("dx", "-.8em")
            .attr("dy", ".15em");

        // add the y Axis
        svg.append("g")
            .call(d3.axisLeft(y_pr));

    });
}

function formatprediction(jsonData) {
    var re = /(confidence|prediction)(\()(.*)(\))/;
    // var jsonData =  [{"StudentID":"2609","confidence(-1)":0.04515376974157927,"confidence(0)":0.1520913538217355,"confidence(1)":0.21643937595225024,"confidence(2)":0.14204776776852118,"confidence(B)":0.12291016148314096,"confidence(D)":0.321357571232773,"prediction(Doorloop: Studieduur)":"D"}];

    var data = [];

    for (var i in jsonData) {
        var keys = Object.keys(jsonData[i]);
        var values = Object.values(jsonData[i]);
        var studentid = values[0];
        var prediction = values[values.length - 1];

        keys.shift();
        keys.pop();
        values.shift();
        values.pop();
        for (var j = 0; j < keys.length; j++) {
            var confidence = keys[j];
            var parts = confidence.match(re)
            var type = parts[1]
            var outcome = parts[3];
            var obj = {};
            obj['Attribute'] = outcome;
            obj['Weight'] = +values[j];
            data.push(obj);
        }
    }
    return data;
}
