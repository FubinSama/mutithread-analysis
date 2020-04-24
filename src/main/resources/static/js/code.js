window.onload = function () {
    var preElements = document.getElementsByTagName("pre");
    for (var i = 0; i < preElements.length; i++) {
        var target = preElements.item(i);
        if (target.className != "sourceCode") {
            continue;
        }
        var content = target.firstChild.nodeValue;
        var linesArray = content.split(String.fromCharCode(13));
        if (linesArray.length == 1) {
            linesArray = content.split(String.fromCharCode(10));
        }

        var olContainer = document.createElement("ol");
        var index = 1;
        for (var j = 0; j < linesArray.length; j++) {
            var liContainer = document.createElement("li");
            liContainer.className = index++ % 2 ? "odd" : "even";
            var spanContainer = document.createElement("span");
            var aText = document.createTextNode(linesArray[j]);
            spanContainer.appendChild(aText);
            liContainer.appendChild(spanContainer);
            olContainer.appendChild(liContainer);
        }
        while (target.firstChild) {
            target.removeChild(target.firstChild);
        }

        target.appendChild(olContainer);
    }
}