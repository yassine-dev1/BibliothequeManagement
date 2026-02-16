//<![CDATA[
function handleAjaxMessages(data) {
    if (data.status === 'success') {
        // Utilisation d'un sélecteur de classe pour être indépendant des IDs JSF complexes
        var msgBlocks = document.getElementsByClassName('jsf-msg');
        for(var i=0; i < msgBlocks.length; i++) {
            var el = msgBlocks[i];
            if (el && el.innerText.trim() !== "") {
                el.style.display = 'block';
                el.style.opacity = '1';
                setTimeout(function() {
                    el.style.transition = "opacity 1s ease";
                    el.style.opacity = "0";
                    setTimeout(function() { el.style.display = 'none'; }, 1000);
                }, 3000);
            }
        }
    }
}
//]]>