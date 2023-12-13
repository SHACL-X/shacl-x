function germanLabel($value) {
    let results = [];
    let p = TermFactory.namedNode("http://example.org/ns#germanLabel");
    let s = $data.find($value, p, null);
    for (let t = s.next(); t; t = s.next()) {
        let object = t.getObject();
        if (object.getTermType() !== "Literal" || !object.getLanguage().startsWith("de")) {
            results.push({
                value: object
            });
        }
    }
    return results;
}
