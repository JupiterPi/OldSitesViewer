function loadMeta(pageName) {
    var meta = getJSONSync("/meta/" + pageName);
    document.title = meta.title;
}