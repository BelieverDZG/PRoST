SELECT * 
WHERE {
        ?num <http://example.org/firstnum> ?a
        MINUS {
                ?num <http://example.org/secondnum> ?b.
                FILTER(?a = ?b)
        }
}