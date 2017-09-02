/**
 * D3 JavaScript that create an interactive ROC Curve
 */
function plot_roc_chart(url, element, title) {
    // set the dimensions and margins of the graph
    var $container = $(element), width = $container.width();

    var margin = {top: 30, right: 30, bottom: 30, left: 30},
        width = width - margin.left - margin.right,
        height = 200 - margin.top - margin.bottom;

    d3.select(element).select("svg").remove();

    var svg = d3.select(element).append("svg")
        .attr("width", '100%')
        .attr("height", height + margin.top + margin.bottom)
        .append("g");
    //.attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    d3.json(url, function (error, data) {
        if (error) throw error;

        // Define the div for the tooltip
        var tooltip = d3.select('body').append('div')
            .attr('class', 'tooltip')
            .style('opacity', 0);

        var maxTpr = d3.max(data, function (d) {
            return d.tpr;
        });
        var maxFpr = d3.max(data, function (d) {
            return d.fpr;
        });

        var x = d3.scaleLinear()
            .domain([0, maxFpr])
            .range([0, width]);

        var y = d3.scaleLinear()
            .domain([0, maxTpr])
            .range([height, 0]);

        var xAxis = d3.axisBottom(x);
        var yAxis = d3.axisLeft(y);

        var rocChart = svg.append('g').attr('class', 'roc_chart')
            .attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');

        // create path group
        var pathGroup = rocChart.append('g').attr('class', 'roc_path');
        var lineFunction = d3.line()
            .x(function (d) {
                return x(d.fpr);
            })
            .y(function (d) {
                return y(d.tpr);
            });

        var areaFunction = d3.area()
            .x(function (d) {
                return x(d.fpr);
            })
            .y0(height)
            .y1(function (d) {
                return y(d.tpr);
            });

        //var totalLength = areaFunction.node().getTotalLength();

        pathGroup.append('path')
            .attr('d', areaFunction(data));

        pathGroup.select('path')
            .on('mouseover', function () {
                pathGroup.attr('stroke', 'red');
                pathGroup.attr('stroke-width', '2px')
            })
            .on('mouseout', function () {
                pathGroup.attr('stroke-width', '0px')
            });

        // create dots group
        var dotsGroup = rocChart.append('g').attr('class', 'roc_dots');

        // Add the dots group
        dotsGroup.selectAll('dot')
            .data(data)
            .enter().append('circle')
            .attr('r', 2.5)
            .attr('cx', function (d) {
                return x(d.fpr);
            })
            .attr('cy', function (d) {
                return y(d.tpr);
            })
            .on('mouseover', function (d) {
                tooltip.transition()
                    .duration(100)
                    .style('opacity', .9);
                tooltip.html('(' + d.fpr.toFixed(2) + ',' + d.tpr.toFixed(2) + ')')
                    .style('left', (d3.event.pageX) + 'px')
                    .style('top', (d3.event.pageY) + 'px');
            })
            .on('mouseout', function (d) {
                tooltip.transition()
                    .duration(100)
                    .style('opacity', 0);
            });

        // add axis
        rocChart.append('g')
            .attr('class', 'x axis')
            .attr('transform', 'translate(0,' + height + ')')
            .call(xAxis);

        rocChart.append('g')
            .attr('class', 'y axis')
            .call(yAxis);

        // gridlines in y axis function
        function make_y_gridlines() {
            return d3.axisLeft(y)
                .ticks(5)
        }

        // add the Y gridlines
        rocChart.append("g")
            .attr("class", "grid")
            .call(make_y_gridlines()
                .tickSize(-width)
                .tickFormat("")
            );


        var focus = rocChart.append('g').style('display', 'none');

        focus.append('line')
            .attr('id', 'selector')
            .attr('class', 'focusLine');

        //rocChart.append('rect').attr('class','overlay').attr('width', width).attr('height',height)
        rocChart
            .on('mouseover', function () {
                focus.style('display', null);
            })
            .on('mouseout', function () {
                focus.style('display', 'none');
            })
            .on('mousemove', function () {
                var mouse = d3.mouse(this);
                focus.select('#selector')
                    .attr('x1', mouse[0]).attr('y1', 0)
                    .attr('x2', mouse[0]).attr('y2', height);
            });

        rocChart
            .on('mouseover', function () {
                focus.style('display', null);
            })
            .on('mouseout', function () {
                focus.style('display', 'none');
            })
            .on('mousemove', function () {
                var mouse = d3.mouse(this);
                focus.select('#selector')
                    .attr('x1', mouse[0]).attr('y1', 0)
                    .attr('x2', mouse[0]).attr('y2', height);
            });

        rocChart.append("text")
            .attr("x", (width / 2))
            .attr("y", 0 - (margin.top / 2))
            .attr("text-anchor", "middle")
            .style("font-size", "12px")
            .text(title);

    });
}