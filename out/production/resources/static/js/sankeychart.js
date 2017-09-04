function plot_sankey(url) {
    var units = "Students";

    // set the dimensions and margins of the graph
    var $container = $('#sankeychart'), width = $container.parent().width();

    // set the dimensions and margins of the graph
    var margin = {top: 30, right: 30, bottom: 0, left: 0},
        width = width - margin.left - margin.right,
        height = 400 - margin.top - margin.bottom;

    // format variables
    var formatNumber = d3.format(",.0f"),    // zero decimal places
        format = function (d) {
            return formatNumber(d) + " " + units;
        };

    // create colors for nodes
    var color = d3.scaleOrdinal(['#000000', '#00FF00', '#FF0000', '#FFA500']);

    // append the svg object to the body of the page
    d3.select("#sankeychart").select("svg").remove();

    var svg = d3.select("#sankeychart").append("svg")
    //.attr("width", width + margin.left + margin.right)
        .attr("width", "100%")
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform",
            "translate(" + margin.left + "," + margin.top + ")");

    // Set the sankey diagram properties
    var sankey = d3.sankey()
        .nodeWidth(36)
        .nodePadding(40)
        .size([width, height]);

    var path = sankey.link();

    // load the data
    d3.json(url, function (error, data) {
        if (error) throw error;

        var graph = sankeyformat(data);

        sankey
            .nodes(graph.nodes)
            .links(graph.links)
            .layout(32);

        // add in the links
        var link = svg.append("g").selectAll(".link")
            .data(graph.links)
            .enter().append("path")
            .attr("class", "link")
            .attr("d", path)
            .style("stroke-width", function (d) {
                return Math.max(1, d.dy);
            })
            .sort(function (a, b) {
                return b.dy - a.dy;
            });

        // add the link titles
        link.append("title")
            .text(function (d) {
                return d.source.name + " → " +
                    d.target.name + "\n" + format(d.value);
            });

        // add in the nodes
        var node = svg.append("g").selectAll(".node")
            .data(graph.nodes)
            .enter().append("g")
            .attr("class", "node")
            .attr("transform", function (d) {
                return "translate(" + d.x + "," + d.y + ")";
            })
            .call(d3.drag()
                .subject(function (d) {
                    return d;
                })
                .on("start", function () {
                    this.parentNode.appendChild(this);
                })
                .on("drag", dragmove));

        // add the rectangles for the nodes
        node.append("rect")
            .attr("height", function (d) {
                return d.dy;
            })
            .attr("width", sankey.nodeWidth())
            .style("fill", function (d) {
                return d.color = color(d.name.replace(/ .*/, ""));
            })
            .style("stroke", function (d) {
                return d3.rgb(d.color).darker(2);
            })
            .append("title")
            .text(function (d) {
                return d.name + "\n" + format(d.value);
            });

        // add in the title for the nodes
        node.append("text")
            .attr("x", -6)
            .attr("y", function (d) {
                return d.dy / 2;
            })
            .attr("dy", ".35em")
            .attr("text-anchor", "end")
            .attr("transform", null)
            .text(function (d) {
                return d.name;
            })
            .filter(function (d) {
                return d.x < width / 2;
            })
            .attr("x", 6 + sankey.nodeWidth())
            .attr("text-anchor", "start");

// the function for moving the nodes
        function dragmove(d) {
            d3.select(this)
                .attr("transform",
                    "translate("
                    + d.x + ","
                    + (d.y = Math.max(
                        0, Math.min(height - d.dy, d3.event.y))
                    ) + ")");
            sankey.relayout();
            link.attr("d", path);
        }
    });
}

function sankeyformat(data) {
    var stats = {'Red': 0, 'Green': 0, 'Orange': 0};
    var output = {
        "nodes": [
            {
                "node": 0,
                "name": "Starter"
            },
            {
                "node": 1,
                "name": "Green"
            },
            {
                "node": 2,
                "name": "Red"
            },
            {
                "node": 3,
                "name": "Orange"
            }],
        "links": []
    };

    for (var key in Object.keys(data)) {
        switch (data[key].Track) {
            case 'Red':
                stats['Red']++;
                break;
            case 'Green':
                stats['Green']++;
                break;
            default:
                stats['Orange']++;
        }
    }

    for (var track in stats) {
        var link = {
            "source": 0,
            "target": 0,
            "value": 0
        };
        switch (track) {
            case 'Red':
                link.source = 0;
                link.target = 1;
                link.value = stats[track];
                break;
            case 'Green':
                link.source = 0;
                link.target = 2;
                link.value = stats[track];
                break;
            case 'Orange':
                link.source = 0;
                link.target = 3;
                link.value = stats[track];
                break;
        }
        output.links.push(link);
    }
    return output;
}