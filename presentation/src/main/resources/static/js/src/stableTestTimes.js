var data;
d3.json("/api3", function(d) {
	data = d;
	var margin = {
    top: 20,
    right: 20,
    bottom: 70,
    left: 40
  },
  width = 600 - margin.left - margin.right,
  height = (Object.keys(data).length)*32;


	var svg = d3.select("body").append("svg")
	  .attr("width", width + margin.left + margin.right)
	  .attr("height", height + margin.top + margin.bottom)
	  .append("g")
	  .attr("transform",
	    "translate(" + margin.left + "," + margin.top + ")");
	
	
	svg.selectAll("bar")
	  .data(data)
	  .enter().append("rect")
	  .attr("x", 10)
	  .attr("width", 700)
	  .attr("y", function(d, i) {
	    return i * 32
	  })
	  .attr("height", 30)
	  .attr("fill", function(d, i) {
	    return i % 2 ? "#83adef" : "lightblue";
	  });
	
	svg.selectAll("second-bar")
	  .data(data)
	  .enter().append("rect")
	  .style("fill", function(d) {
	    return d.status == 0 ? "yellow" : "green";
	  })
	  .attr("x", 500)
	  .attr("width", 20)
	  .attr("y", function(d, i) {
	    return i * 32
	  })
	  .attr("height", 30);
	
	
	var yTextPadding = 20;
	svg.selectAll(".bartext")
	  .data(data)
	  .enter()
	  .append("text")
	  .attr("class", "bartext")
	  .attr("text-anchor", "middle")
	  .attr("fill", "white")
	  .attr("x", 250)
	  .attr("y", function(d, i) {
	    return (i * 32) + 22;
	  })
	  .text(function(d) {
	    return d.name + " : " + d.status;
	  })
	  .attr("fill", "black");
	
	});


