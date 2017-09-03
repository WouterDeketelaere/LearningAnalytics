var $container = $('#clusterstab'), width = $container.parent().width() * 0.4;
var height = width;


// Config for the Radar chart
var config = {
    w: width,
    h: height,
    maxValue: 1,
    levels: 5,
    ExtraWidthX: 300
};

console.log("Width = " + width);
var w = width,
    h = 500;

var colorscale = d3.scaleOrdinal(d3.schemeCategory10);
var LegendOptions = [];
var j = 0;
var expr1 = /average\((.*)\)/;
var expr2 = /count\((.*)\)/;
var output = [];

function plotRadarPlot(id, url, number) {
    //Call function to draw the Radar chart
    d3.json(url, function (error, input) {
        if (error) throw error;
        var data = createAxes(input, number);
        RadarChart.draw(id, data, config);
        addLegend(id);
    });

}

function addLegend(id) {
    ////////////////////////////////////////////
    /////////// Initiate legend ////////////////
    ////////////////////////////////////////////
    var bodyid = id === '#radarchart1' ? '#body1' : '#body2';
    console.log("Legend options = " + LegendOptions + " body id=" + bodyid);

    var svg = d3.select(bodyid)
        .select('.radarchart')
        .append('svg')
        .attr('class', 'legendwindow')
        .attr("width", w + 300)
        .attr("height", h);

    //Initiate Legend
    var legend = svg.append("g")
        .attr("class", "legend")
        .attr("height", 100)
        .attr("width", 200)
        .attr('transform', 'translate(100,20)')
    ;
    //Create colour squares
    legend.selectAll('rect')
        .data(LegendOptions)
        .enter()
        .append("rect")
        .attr("x", w - 65)
        .attr("y", function (d, i) {
            return i * 20;
        })
        .attr("width", 10)
        .attr("height", 10)
        .style("fill", function (d, i) {
            return colorscale(i);
        })
    ;
    //Create text next to squares
    legend.selectAll('text')
        .data(LegendOptions)
        .enter()
        .append("text")
        .attr("x", w - 52)
        .attr("y", function (d, i) {
            return i * 20 + 9;
        })
        .attr("font-size", "11px")
        .attr("fill", "#737373")
        .text(function (d) {
            return d;
        });
}

// extract cluster data from set of clusters
function extractCluster(input, n) {
    var output = [];
    for (var i = n - 1; i < n + 2; i++) {
        output.push(input[i]);
    }
    return output;
}

// parse cluster data to create axis information
function createAxis(cluster) {
    var axis = [];

    LegendOptions.push(Object.values(cluster)[0]);
    for (var i = 1; i < Object.keys(cluster).length; i++) {
        // if value contains average(...)
        if (expr1.test(Object.keys(cluster)[i])) {
            axis.push(
                {
                    "axis": expr1.exec(Object.keys(cluster)[i])[1],
                    "value": roundValue(Object.values(cluster)[i], 3)
                }
            );
        }
        // if value contains count(...)
        if (expr2.test(Object.keys(cluster)[i])) {

            LegendOptions[j] = LegendOptions[j]+" (" + Object.values(cluster)[i] + ")";
            j++;
        }
    }
    return axis;
}

// create cluster axes and cluster counts for specified cluster (1 -> 4)
function createAxes(clusters, n) {
    LegendOptions = [];
    j = 0;
    var cluster = extractCluster(clusters, n);
    var output = [];
    for (var i = 0; i < cluster.length; i++) {
        output.push(createAxis(cluster[i]));
    }
    return output;
}
