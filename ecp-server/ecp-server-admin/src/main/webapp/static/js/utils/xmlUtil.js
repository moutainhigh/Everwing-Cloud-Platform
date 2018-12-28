function xmlBegin(){
 return '<?xml version="1.0"?>';
}
function tagBegin(tagName){
 return '<'+tagName+'>';
}
function tagEnd(tagName){
 return '</'+tagName+'>';
}
function createElement(tagName,tagValue){
return '<'+tagName+'>'+tagValue+'</'+tagName+'>';
}