NodeFactory.registerNamespace("ex", "http://datashapes.org/js/tests/rules/rectangle-rdfquery.test#");

function computeArea($this) {
	return $data.query().
				match($this, "ex:width", "?width").
				match($this, "ex:height", "?height").
				bind("?area", function(sol) { return T(sol.width.getLex() * sol.height.getLex()) }).
				construct($this, "ex:area", "?area");
}