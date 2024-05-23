const NS = "http://datashapes.org/js/tests/rules/rectangle.test#";

function computeArea($this) {
    let width = getProperty($this, "width");
    let height = getProperty($this, "height");
    let area = TermFactory.literal(width.getLex() * height.getLex(), width.getDatatype());
    let areaProperty = TermFactory.namedNode(NS + "area");
    return [ [$this, areaProperty, area] ];
}

function getProperty($this, name) {
    let it = $data.find($this, TermFactory.namedNode(NS + name), null);
    let result = it.next().getObject();
    it.close();
    return result;
}