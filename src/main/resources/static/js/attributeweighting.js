$('#fc_slider_attribute').slider({});

// This function (re)draws a barchart from the attribute weighting data
function plot_attribute_weights(url) {
    // set the dimensions and margins of the graph
    var $container = $('.attributeweights'), width = $container.width();

    var margin = {top: 20, right: 20, bottom: 150, left: 40},
        width = width - margin.left - margin.right,
        height = 500 - margin.top - margin.bottom;

    // set the ranges
    var x = d3.scaleBand()
        .range([0, width])
        .padding(0.1);
    var y = d3.scaleLinear()
        .range([height, 0]);

    d3.select(".attributeweights").select("svg").remove();

    var svg = d3.select(".attributeweights").append("svg")
        .attr("width", '100%')
        .attr("height", height + margin.top + margin.bottom)
        //.attr('viewBox','0 0 '+Math.min(width,height)+' '+Math.min(width,height))
        // .attr('preserveAspectRatio','xMinYMin')
        //.attr("width", width + margin.left + margin.right)
        //.attr("transform", "translate(" + Math.min(width,height) / 2 + "," + Math.min(width,height) / 2 + ")");
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    var tool_tip = d3.tip()
        .attr("class", "d3-tip")
        .offset([-8, 0])
        .html(function (d) {
            return d.Attribute + "<br>Weight: " + roundValue(d.Weight, 3);
        });
    svg.call(tool_tip);

    d3.json(url, function (error, data) {
        if (error) throw error;

        // Format the data
        data.forEach(function (d) {
            d.Weight = +d.Weight;
        });

        // Scale the range of the data in the domains
        x.domain(data.map(function (d) {
            return d.Attribute;
        }));
        y.domain([0, d3.max(data, function (d) {
            return d.Weight;
        })]);

        // Append the rectangles for the bar chart
        svg.selectAll(".bar")
            .data(data)
            .enter().append("rect")
            .attr("class", "bar")
            .attr("x", function (d) {
                return x(d.Attribute);
            })
            .attr("width", x.bandwidth())
            .attr("y", function (d) {
                return y(d.Weight);
            })
            .attr("height", function (d) {
                return height - y(d.Weight);
            })
            .on('mouseover', tool_tip.show)
            .on('mouseout', tool_tip.hide);

        // add the x Axis
        svg.append("g")
            .attr("transform", "translate(0," + height + ")")
            .call(d3.axisBottom(x))
            .call(d3.axisBottom(x))
            .selectAll("text")
            .style("text-anchor", "end")
            .attr("dx", "-.8em")
            .attr("dy", ".15em")
            .attr("transform", function (d) {
                return "rotate(-65)"
            });

        // add the y Axis
        svg.append("g")
            .call(d3.axisLeft(y));

    });
}

