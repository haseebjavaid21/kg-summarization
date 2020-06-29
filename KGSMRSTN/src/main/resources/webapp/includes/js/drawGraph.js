
d3.select(self.frameElement).style("height", "180px");
d3.select(self.frameElement).style("width", "120px");

function drawCluster(subjectNode, dataSet, typeLongHash, nodesRank, domElement ) {

    /**
     * Color Scale
     * Possible Values:
     * - d3.scale.category10()
     * - d3.scale.category20()
     * - d3.scale.category20b()
     * - d3.scale.category20c()
     */

     console.log();

    nodeSet = dataSet.nodes;
    linkSet = dataSet.edges; 


    colorScale = d3.scale.category10();

    var width = $( domElement ).width();
    var height = window.innerHeight;


    var subjectNodeRad = 50;
    var objNodeRad = 10;
    var colorMap = [];

    var typeMouseOver = function() {
    
      var thisObject = d3.select(this);
      var typeValue = thisObject.attr("type_value");
      var strippedTypeValue = typeValue.replace(/ /g, "_");

      var legendBulletSelector = "." + "legendBullet-" + strippedTypeValue;
      var selectedBullet = d3.selectAll(legendBulletSelector);
      //document.writeln(legendBulletSelector);
      selectedBullet.style("fill", "Maroon");
  selectedBullet.attr("r", 1.2*6);

      var legendTextSelector = "." + "legendText-" + strippedTypeValue;
      var selectedLegendText = d3.selectAll(legendTextSelector);
      //document.writeln(legendBulletSelector);
      selectedLegendText.style("font", "bold 14px Arial")
      selectedLegendText.style("fill", "Maroon");

      var nodeTextSelector = "." + "nodeText-" + strippedTypeValue;
      var selectedNodeText = d3.selectAll(nodeTextSelector);
      //document.writeln(pie3SliceSelector);
      selectedNodeText.style("font", "bold 16px Arial")
      selectedNodeText.style("fill", "Maroon");

      var nodeCircleSelector = "." + "nodeCircle-" + strippedTypeValue;
      var selectedCircle = d3.selectAll(nodeCircleSelector);
      //document.writeln(nodeCircleSelector);
      selectedCircle.style("fill", "Maroon");
      selectedCircle.style("stroke", "Maroon");
  selectedCircle.attr("r", 1.2*objNodeRad);

      var subjectNodeCircleSelector = "." + "subjectNodeCircle";
      var selectedSubjectNodeCircle = d3.selectAll(subjectNodeCircleSelector);
      //document.writeln(subjectNodeCircleSelector);
      var subjectNodeType = selectedSubjectNodeCircle.attr("type_value");
      if (typeValue == subjectNodeType){
        selectedSubjectNodeCircle.style("stroke", "Maroon");
        selectedSubjectNodeCircle.style("fill", "White");
      };

      var subjectNodeTextSelector = "." + "subjectNodeText";
      var selectedSubjectNodeText = d3.selectAll(subjectNodeTextSelector);
      var subjectNodeTextType = selectedSubjectNodeText.attr("type_value");
      //document.writeln(pie3SliceSelector);
      if (typeValue == subjectNodeTextType) {
        selectedSubjectNodeText.style("fill", "Maroon");
        selectedSubjectNodeText.style("font", "bold 16px Arial")
      };

    };

    var typeMouseOut = function() {

      var thisObject = d3.select(this);
      var typeValue = thisObject.attr("type_value");
      var colorValue = thisObject.attr("color_value");
      var strippedTypeValue = typeValue.replace(/ /g, "_");

      var legendBulletSelector = "." + "legendBullet-" + strippedTypeValue;
      var selectedBullet = d3.selectAll(legendBulletSelector);
      //document.writeln(legendBulletSelector);
      selectedBullet.style("fill", colorValue);
  selectedBullet.attr("r", 6);

      var legendTextSelector = "." + "legendText-" + strippedTypeValue;
      var selectedLegendText = d3.selectAll(legendTextSelector);
      //document.writeln(legendBulletSelector);
      selectedLegendText.style("font", "normal 14px Arial")
      selectedLegendText.style("fill", "Black");

      var nodeTextSelector = "." + "nodeText-" + strippedTypeValue;
      var selectedNodeText = d3.selectAll(nodeTextSelector);
      //document.writeln(pie3SliceSelector);
      selectedNodeText.style("font", "normal 16px Arial")
      selectedNodeText.style("fill", "Blue");

      var nodeCircleSelector = "." + "nodeCircle-" + strippedTypeValue;
      var selectedCircle = d3.selectAll(nodeCircleSelector);
      //document.writeln(nodeCircleSelector);
      selectedCircle.style("fill", "White");
      selectedCircle.style("stroke", colorValue);
  selectedCircle.attr("r", objNodeRad);

      var subjectNodeCircleSelector = "." + "subjectNodeCircle";
      var selectedSubjectNodeCircle = d3.selectAll(subjectNodeCircleSelector);
      //document.writeln(subjectNodeCircleSelector);
      var subjectNodeType = selectedSubjectNodeCircle.attr("type_value");
      if (typeValue == subjectNodeType){
        selectedSubjectNodeCircle.style("stroke", colorValue);
        selectedSubjectNodeCircle.style("fill", "White");
      };

      var subjectNodeTextSelector = "." + "subjectNodeText";
      var selectedSubjectNodeText = d3.selectAll(subjectNodeTextSelector);
      //document.writeln(pie3SliceSelector);
      selectedSubjectNodeText.style("fill", "Blue");
      selectedSubjectNodeText.style("font", "normal 14px Arial")

    };

    var rankMouseOver = function() {
    
      var thisObject = d3.select(this);
      var typeValue = thisObject.attr("type_value");
      var strippedTypeValue = typeValue.replace(/ /g, "_");

      var legendBulletSelector = "." + "legendBullet-" + strippedTypeValue;
      var selectedBullet = d3.selectAll(legendBulletSelector);
      //document.writeln(legendBulletSelector);
      selectedBullet.style("fill", "Maroon");
  selectedBullet.attr("r", 1.2*6);

      var legendTextSelector = "." + "legendText-" + strippedTypeValue;
      var selectedLegendText = d3.selectAll(legendTextSelector);
      //document.writeln(legendBulletSelector);
      selectedLegendText.style("font", "bold 14px Arial")
      selectedLegendText.style("fill", "Maroon");

      var nodeTextSelector = "." + "nodeText-" + strippedTypeValue;
      var selectedNodeText = d3.selectAll(nodeTextSelector);
      //document.writeln(pie3SliceSelector);
      selectedNodeText.style("font", "bold 16px Arial")
      selectedNodeText.style("fill", "Maroon");

      var nodeCircleSelector = "." + "nodeCircle-" + strippedTypeValue;
      var selectedCircle = d3.selectAll(nodeCircleSelector);
      //document.writeln(nodeCircleSelector);
      selectedCircle.style("fill", "Maroon");
      selectedCircle.style("stroke", "Maroon");
  selectedCircle.attr("r", 1.2*objNodeRad);

      var subjectNodeCircleSelector = "." + "subjectNodeCircle";
      var selectedSubjectNodeCircle = d3.selectAll(subjectNodeCircleSelector);
      //document.writeln(subjectNodeCircleSelector);
      var subjectNodeType = selectedSubjectNodeCircle.attr("type_value");
      if (typeValue == subjectNodeType){
        selectedSubjectNodeCircle.style("stroke", "Maroon");
        selectedSubjectNodeCircle.style("fill", "White");
      };

      var subjectNodeTextSelector = "." + "subjectNodeText";
      var selectedSubjectNodeText = d3.selectAll(subjectNodeTextSelector);
      var subjectNodeTextType = selectedSubjectNodeText.attr("type_value");
      //document.writeln(pie3SliceSelector);
      if (typeValue == subjectNodeTextType) {
        selectedSubjectNodeText.style("fill", "Maroon");
        selectedSubjectNodeText.style("font", "bold 16px Arial")
      };

    };

    var rankMouseOut = function() {

      var thisObject = d3.select(this);
      var typeValue = thisObject.attr("type_value");
      var colorValue = thisObject.attr("color_value");
      var strippedTypeValue = typeValue.replace(/ /g, "_");

      var legendBulletSelector = "." + "legendBullet-" + strippedTypeValue;
      var selectedBullet = d3.selectAll(legendBulletSelector);
      //document.writeln(legendBulletSelector);
      selectedBullet.style("fill", colorValue);
  selectedBullet.attr("r", 6);

      var legendTextSelector = "." + "legendText-" + strippedTypeValue;
      var selectedLegendText = d3.selectAll(legendTextSelector);
      //document.writeln(legendBulletSelector);
      selectedLegendText.style("font", "normal 14px Arial")
      selectedLegendText.style("fill", "Black");

      var nodeTextSelector = "." + "nodeText-" + strippedTypeValue;
      var selectedNodeText = d3.selectAll(nodeTextSelector);
      //document.writeln(pie3SliceSelector);
      selectedNodeText.style("font", "normal 16px Arial")
      selectedNodeText.style("fill", "Blue");

      var nodeCircleSelector = "." + "nodeCircle-" + strippedTypeValue;
      var selectedCircle = d3.selectAll(nodeCircleSelector);
      //document.writeln(nodeCircleSelector);
      selectedCircle.style("fill", "White");
      selectedCircle.style("stroke", colorValue);
  selectedCircle.attr("r", objNodeRad);

      var subjectNodeCircleSelector = "." + "subjectNodeCircle";
      var selectedSubjectNodeCircle = d3.selectAll(subjectNodeCircleSelector);
      //document.writeln(subjectNodeCircleSelector);
      var subjectNodeType = selectedSubjectNodeCircle.attr("type_value");
      if (typeValue == subjectNodeType){
        selectedSubjectNodeCircle.style("stroke", colorValue);
        selectedSubjectNodeCircle.style("fill", "White");
      };

      var subjectNodeTextSelector = "." + "subjectNodeText";
      var selectedSubjectNodeText = d3.selectAll(subjectNodeTextSelector);
      //document.writeln(pie3SliceSelector);
      selectedSubjectNodeText.style("fill", "Blue");
      selectedSubjectNodeText.style("font", "normal 14px Arial")

    };

    var nodeMouseOver = function() {
      var thisObject = d3.select(this);
      var typeValue = thisObject.attr("type_value");
      var colorValue = thisObject.attr("color_value");
      var strippedTypeValue = typeValue.replace(/ /g, "_");

      d3.select(this).select("circle").transition()
          .duration(250)
      .attr("r", function(d,i) { if(d.id==subjectNodeID) {return 65;} else {return 15;} } );
      d3.select(this).select("text")
        .style("font", "bold 20px Arial")
        .attr("fill", "Blue");

      var legendBulletSelector = "." + "legendBullet-" + strippedTypeValue;
      var selectedBullet = d3.selectAll(legendBulletSelector);
      //document.writeln(legendBulletSelector);
      selectedBullet.style("fill", "Maroon");
  selectedBullet.attr("r", 1.2*6);

      var legendTextSelector = "." + "legendText-" + strippedTypeValue;
      var selectedLegendText = d3.selectAll(legendTextSelector);
      //document.writeln(legendBulletSelector);
      selectedLegendText.style("font", "bold 14px Arial")
      selectedLegendText.style("fill", "Maroon");

    }

    var nodeMouseOut = function() {
      var thisObject = d3.select(this);
      var typeValue = thisObject.attr("type_value");
      var colorValue = thisObject.attr("color_value");
      var strippedTypeValue = typeValue.replace(/ /g, "_");

      d3.select(this).select("circle").transition()
          .duration(250)
      .attr("r", function(d,i) { if(d.id==subjectNodeID) {return subjectNodeRad;} else {return objNodeRad;} } );
  d3.select(this).select("text")
      .style("font", "normal 16px Arial")
      .attr("fill", "Blue");

      var legendBulletSelector = "." + "legendBullet-" + strippedTypeValue;
      var selectedBullet = d3.selectAll(legendBulletSelector);
      //document.writeln(legendBulletSelector);
      selectedBullet.style("fill", colorValue);
  selectedBullet.attr("r", 6);

      var legendTextSelector = "." + "legendText-" + strippedTypeValue;
      var selectedLegendText = d3.selectAll(legendTextSelector);
      //document.writeln(legendBulletSelector);
      selectedLegendText.style("font", "normal 14px Arial")
      selectedLegendText.style("fill", "Black");

    }

    var nodeClick = function() {
      var thisObject = d3.select(this);
      var typeValue = thisObject.attr("type_value");
      var colorValue = thisObject.attr("color_value");
      $('#ex1').modal({
      });
      console.log(thisObject);
    }

    // list of colors maping to types
    nodeSet.forEach(function(d, i) {
        colorMap[d.type] = d.type;
    });

    function keys(obj)
    {
      var keys = [];

      for(var key in obj)
      {
        if(obj.hasOwnProperty(key))
        {
          keys.push(key);
        }
      }
      return keys;
    }

    var sortedKeys = keys(colorMap).sort();

    sortedKeys.forEach(function(d, i) {
        colorMap[d] = colorScale(i);
    });

    // assigning colors to nodes
    nodeSet.forEach(function(d,i) {
      d.color = colorMap[d.type];
    });
    
// Create a canvas...
    var svgCanvas = d3.select(domElement)
      .append("svg:svg")
        .attr("width", width)
    .attr("height", height)
      .append("svg:g")
        .attr("class", "subjectNodeCanvas")
        .attr("transform", "translate(" + width/2 + "," + height/2 + ")")

    var node_hash = [];
    var type_hash = [];

    // Create a hash that allows access to each node by its id
    nodeSet.forEach(function(d, i) {
      node_hash[d.id] = d;
      type_hash[d.type] = d.type;
    });
  
    // Append the source object node and the target object node to each link records...
    linkSet.forEach(function(d, i) {
      d.source = node_hash[d.sourceId];
      d.target = node_hash[d.targetId];
      if (d.sourceId == subjectNodeID)
    { d.direction = "OUT"; }
      else
    { d.direction = "IN"; }
    });

    // Create a force layout and bind Nodes and Links
    var force = d3.layout.force()
        .nodes(nodeSet)
        .links(linkSet)
        .charge(-1000)
    .gravity(.01)
    .friction(.2)
        .linkStrength(9)
        //.size([width/8, height/10])
        .linkDistance( function(d) { if (width < height) { return width*1/3; } else { return height*1/3 } } ) // Controls edge length
        .on("tick", tick)
        .start();

    // Draw lines for Links between Nodes
    var link = svgCanvas.selectAll(".gLink")
        .data(force.links())
      .enter().append("g")
        .attr("class", "gLink")
      .append("line")
        .attr("class", "link")
        .style("stroke", "#ccc")
        .attr("x1", function(d) { return d.source.x; })
        .attr("y1", function(d) { return d.source.y; })
        .attr("x2", function(d) { return d.target.x; })
        .attr("y2", function(d) { return d.target.y; });

    // Create Nodes
    var node = svgCanvas.selectAll(".node")
        .data(force.nodes())
      .enter().append("g")
        .attr("class", "node")
        .attr("type_value", function(d, i) { return d.type; })
        .attr("color_value", function(d, i) { return colorMap[d.type]; })
//.attr("fixed", function(d) { if (d.id==subjectNodeID) { return true; } else { return false; } } )
        .on("mouseover", nodeMouseOver)
        .on("mouseout", nodeMouseOut)
        .on("click", nodeClick)
        .call(force.drag)
  .append("a")
    .attr("xlink:href", function(d) {return d.hlink; });

    // Append circles to Nodes
    node.append("circle")
//.attr("x", function(d) { return d.x; })
//.attr("y", function(d) { return d.y; })
        .attr("r", function(d) { if (d.id==subjectNodeID) { return subjectNodeRad; } else { return objNodeRad; } } )
        .style("fill", "White") // Make the nodes hollow looking
        .attr("type_value", function(d, i) { return d.type; })
        .attr("color_value", function(d, i) { return colorMap[d.type]; })
//.attr("fixed", function(d) { if (d.id==subjectNodeID) { return true; } else { return false; } } )
//.attr("x", function(d) { if (d.id==subjectNodeID) { return width/2; } else { return d.x; } })
//.attr("y", function(d) { if (d.id==subjectNodeID) { return height/2; } else { return d.y; } })
        .attr("class", function(d, i) {
          var str = d.type;
          var strippedString = str.replace(/ /g, "_")
          //return "nodeCircle-" + strippedString; })
      if (d.id==subjectNodeID) { return "subjectNodeCircle"; }
      else { return "nodeCircle-" + strippedString; }
        })
        .style("stroke-width", 5) // Give the node strokes some thickness
        .style("stroke", function(d, i) { return colorMap[d.type]; } ) // Node stroke colors
    .call(force.drag);

    // Append text to Nodes
    node.append("text")
        .attr("x", function(d) { if (d.id==subjectNodeID) { return 0; } else {return 20;} } )
        .attr("y", function(d) { if (d.id==subjectNodeID) { return 0; } else {return -10;} } )
    .attr("text-anchor", function(d) { if (d.id==subjectNodeID) {return "middle";} else {return "start";} })
    .attr("font-family", "Arial, Helvetica, sans-serif")
        .style("font", "normal 16px Arial")
        .attr("fill", "Blue")
        .style("fill", function(d, i) { return colorMap[d]; })
        .attr("type_value", function(d, i) { return d.type; })
        .attr("color_value", function(d, i) { return colorMap[d.type]; })
        .attr("class", function(d, i) {
          var str = d.type;
          var strippedString = str.replace(/ /g, "_");
          //return "nodeText-" + strippedString; })
      if (d.id==subjectNodeID) { return "subjectNodeText"; }
      else { return "nodeText-" + strippedString; }
        })
        .attr("dy", ".35em")
        .text(function(d) { return  d.name; });

    // Append text to Link edges
    var linkText = svgCanvas.selectAll(".gLink")
        .data(force.links())
      .append("text")
    .attr("font-family", "Arial, Helvetica, sans-serif")
    .attr("x", function(d) {
        if (d.target.x > d.source.x) { return (d.source.x + (d.target.x - d.source.x)/2); }
        else { return (d.target.x + (d.source.x - d.target.x)/2); }
    })
        .attr("y", function(d) {
        if (d.target.y > d.source.y) { return (d.source.y + (d.target.y - d.source.y)/2); }
        else { return (d.target.y + (d.source.y - d.target.y)/2); }
    })
    .attr("fill", "Black")
        .style("font", "normal 12px Arial")
        .attr("dy", ".35em")
        .text(function(d) { return d.linkName; });


    function tick() {
      link
        .attr("x1", function(d) { return d.source.x; })
        .attr("y1", function(d) { return d.source.y; })
        .attr("x2", function(d) { return d.target.x; })
        .attr("y2", function(d) { return d.target.y; });
  
      node
        .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });

      linkText
    .attr("x", function(d) {
        if (d.target.x > d.source.x) { return (d.source.x + (d.target.x - d.source.x)/2); }
        else { return (d.target.x + (d.source.x - d.target.x)/2); }
    })
    .attr("y", function(d) {
        if (d.target.y > d.source.y) { return (d.source.y + (d.target.y - d.source.y)/2); }
        else { return (d.target.y + (d.source.y - d.target.y)/2); }
    });
    }

    function printPredicates() {
            
        // Print Legend Title...
        svgCanvas.append("text").attr("class","region")
        .text("Color Keys for Predicates")
        .attr("x", -1*(width/2 - 10))
        .attr("y", (-height/7*3))
        .style("fill", "Black")
        .style("font", "bold 16px Arial")
        .attr("text-anchor","start");

        // Plot the bullet circles...
        svgCanvas.selectAll("subjectNodeCanvas")
            .data(sortedKeys).enter().append("svg:circle") // Append circle elements
            .attr("cx", -1*(width/2 - 25))
            .attr("cy", function(d, i) { return (i*20-height/7*3 + 20); } )
            .attr("stroke-width", ".5")
            .style("fill", function(d, i) { return colorMap[d]; })
            .attr("r", 6)
            .attr("color_value", function(d, i) { return colorMap[d]; })
            .attr("type_value", function(d, i) { return d; })
            .attr("index_value", function(d, i) { return "index-" + i; })
            .attr("class", function(d) {
                var str = d;
                var strippedString = str.replace(/ /g, "_")
        return "legendBullet-" + strippedString; })
            .on('mouseover', typeMouseOver)
            .on("mouseout", typeMouseOut);

        // Create legend text that acts as label keys...
        svgCanvas.selectAll("a.legend_link")
            .data(sortedKeys) // Instruct to bind dataSet to text elements
        .enter().append("svg:a") // Append legend elements
            .append("text")
            .attr("text-anchor", "center")
            .attr("x", -1*(width/2 - 40))
            .attr("y", function(d, i) { return (i*20-height/7*3 + 20); } )
            .attr("dx", 0)
            .attr("dy", "4px") // Controls padding to place text in alignment with bullets
            .text(function(d) { return typeLongHash[d];})
            .attr("color_value", function(d, i) { return colorMap[d]; })
            .attr("type_value", function(d, i) { return d; })
            .attr("index_value", function(d, i) { return "index-" + i; })
            .attr("class", function(d) {
                var str = d;
                var strippedString = str.replace(/ /g, "_")
                return "legendText-" + strippedString; })
            .style("fill", "Black")
            .style("font", "normal 14px Arial")
            .on('mouseover', typeMouseOver)
            .on("mouseout", typeMouseOut);
    }

    function printNodeRanks() {
            
        // Print Legend Title...
        svgCanvas.append("text").attr("class","region")
        .text("Node Ranks")
        .attr("x", (width/2 - 300))
        .attr("y", (-height/7*3))
        .style("fill", "Black")
        .style("font", "bold 16px Arial")
        .attr("text-anchor","start");

        // Plot the bullet circles...
        svgCanvas.selectAll("subjectNodeCanvas")
            .data(nodesRank).enter().append("svg:circle") // Append circle elements
            .attr("cx", (width/2 - 285))
            .attr("cy", function(d, i) { return (i*20-height/7*3 + 20); } )
            .attr("stroke-width", ".5")
            .style("fill", function(d, i) { return colorMap[d.type]; })
            .attr("r", 6)
            .attr("color_value", function(d, i) { return colorMap[d.type]; })
            .attr("type_value", function(d, i) { return d.type; })
            .attr("index_value", function(d, i) { return "index-" + i; })
            .attr("class", function(d) {
                var str = d.type;
                var strippedString = str.replace(/ /g, "_")
        return "legendBullet-" + strippedString; })
            .on('mouseover', rankMouseOver)
            .on("mouseout", rankMouseOut);

        // Create legend text that acts as label keys...
        svgCanvas.selectAll("a.legend_link")
            .data(nodesRank) // Instruct to bind dataSet to text elements
        .enter().append("svg:a") // Append legend elements
            .append("text")
            .attr("text-anchor", "center")
            .attr("x", (width/2 - 270))
            .attr("y", function(d, i) { return (i*20-height/7*3 + 20); } )
            .attr("dx", 0)
            .attr("dy", "4px") // Controls padding to place text in alignment with bullets
            .text(function(d, i) { return "#" + (parseInt(i)+1) + " - " + d.node;})
            .attr("color_value", function(d, i) { return colorMap[d.type]; })
            .attr("type_value", function(d, i) { return d.type; })
            .attr("index_value", function(d, i) { return "index-" + i; })
            .attr("class", function(d) {
                var str = d.type;
                var strippedString = str.replace(/ /g, "_")
                return "legendText-" + strippedString; })
            .style("fill", "Black")
            .style("font", "normal 14px Arial")
            .on('mouseover', rankMouseOver)
            .on("mouseout", rankMouseOut);
    }

    printPredicates();
    printNodeRanks();

  };

