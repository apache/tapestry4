dojo.provide("dojo.data.YahooStore");
dojo.require("dojo.data.core.RemoteStore");
dojo.require("dojo.lang.declare");
dojo.require("dojo.io.ScriptSrcIO");
dojo.declare("dojo.data.YahooStore",dojo.data.core.RemoteStore,{_setupQueryRequest:function(_1,_2){
var _3=1;
var _4=1;
if(_1){
_3=_1.start||_3;
_4=_1.count||_4;
}
var _5="http://api.search.yahoo.com/WebSearchService/V1/webSearch?appid=dojo&language=en&query="+_1.query+"&start="+_3+"&results="+_4+"&output=json";
_2.url=_5;
_2.transport="ScriptSrcTransport";
_2.mimetype="text/json";
_2.jsonParamName="callback";
},_resultToQueryMetadata:function(_6){
return _6.ResultSet;
},_resultToQueryData:function(_7){
var _8={};
for(var i=0;i<_7.ResultSet.totalResultsReturned;++i){
var _a=_7.ResultSet.Result[i];
var _b={};
_b["Url"]=[_a.Url];
_b["Title"]=[_a.Title];
_b["Summary"]=[_a.Summary];
var _c=(_7.ResultSet.firstResultPosition-1)+i;
_8[_c.toString()]=_b;
}
return _8;
}});
