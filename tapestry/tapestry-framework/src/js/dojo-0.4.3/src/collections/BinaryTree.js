dojo.provide("dojo.collections.BinaryTree");
dojo.require("dojo.collections.Collections");
dojo.require("dojo.experimental");
dojo.experimental("dojo.collections.BinaryTree");
dojo.collections.BinaryTree=function(_1){
function node(_2,_3,_4){
this.value=_2||null;
this.right=_3||null;
this.left=_4||null;
this.clone=function(){
var c=new node();
if(this.value.value){
c.value=this.value.clone();
}else{
c.value=this.value;
}
if(this.left){
c.left=this.left.clone();
}
if(this.right){
c.right=this.right.clone();
}
};
this.compare=function(n){
if(this.value>n.value){
return 1;
}
if(this.value<n.value){
return -1;
}
return 0;
};
this.compareData=function(d){
if(this.value>d){
return 1;
}
if(this.value<d){
return -1;
}
return 0;
};
}
function inorderTraversalBuildup(_8,a){
if(_8){
inorderTraversalBuildup(_8.left,a);
a.add(_8);
inorderTraversalBuildup(_8.right,a);
}
}
function preorderTraversal(_a,_b){
var s="";
if(_a){
s=_a.value.toString()+_b;
s+=preorderTraversal(_a.left,_b);
s+=preorderTraversal(_a.right,_b);
}
return s;
}
function inorderTraversal(_d,_e){
var s="";
if(_d){
s=inorderTraversal(_d.left,_e);
s+=_d.value.toString()+_e;
s+=inorderTraversal(_d.right,_e);
}
return s;
}
function postorderTraversal(_10,sep){
var s="";
if(_10){
s=postorderTraversal(_10.left,sep);
s+=postorderTraversal(_10.right,sep);
s+=_10.value.toString()+sep;
}
return s;
}
function searchHelper(_13,_14){
if(!_13){
return null;
}
var i=_13.compareData(_14);
if(i==0){
return _13;
}
if(i>0){
return searchHelper(_13.left,_14);
}else{
return searchHelper(_13.right,_14);
}
}
this.add=function(_16){
var n=new node(_16);
var i;
var _19=_1a;
var _1b=null;
while(_19){
i=_19.compare(n);
if(i==0){
return;
}
_1b=_19;
if(i>0){
_19=_19.left;
}else{
_19=_19.right;
}
}
this.count++;
if(!_1b){
_1a=n;
}else{
i=_1b.compare(n);
if(i>0){
_1b.left=n;
}else{
_1b.right=n;
}
}
};
this.clear=function(){
_1a=null;
this.count=0;
};
this.clone=function(){
var c=new dojo.collections.BinaryTree();
c.root=_1a.clone();
c.count=this.count;
return c;
};
this.contains=function(_1d){
return this.search(_1d)!=null;
};
this.deleteData=function(_1e){
var _1f=_1a;
var _20=null;
var i=_1f.compareData(_1e);
while(i!=0&&_1f!=null){
if(i>0){
_20=_1f;
_1f=_1f.left;
}else{
if(i<0){
_20=_1f;
_1f=_1f.right;
}
}
i=_1f.compareData(_1e);
}
if(!_1f){
return;
}
this.count--;
if(!_1f.right){
if(!_20){
_1a=_1f.left;
}else{
i=_20.compare(_1f);
if(i>0){
_20.left=_1f.left;
}else{
if(i<0){
_20.right=_1f.left;
}
}
}
}else{
if(!_1f.right.left){
if(!_20){
_1a=_1f.right;
}else{
i=_20.compare(_1f);
if(i>0){
_20.left=_1f.right;
}else{
if(i<0){
_20.right=_1f.right;
}
}
}
}else{
var _22=_1f.right.left;
var _23=_1f.right;
while(_22.left!=null){
_23=_22;
_22=_22.left;
}
_23.left=_22.right;
_22.left=_1f.left;
_22.right=_1f.right;
if(!_20){
_1a=_22;
}else{
i=_20.compare(_1f);
if(i>0){
_20.left=_22;
}else{
if(i<0){
_20.right=_22;
}
}
}
}
}
};
this.getIterator=function(){
var a=[];
inorderTraversalBuildup(_1a,a);
return new dojo.collections.Iterator(a);
};
this.search=function(_25){
return searchHelper(_1a,_25);
};
this.toString=function(_26,sep){
if(!_26){
var _26=dojo.collections.BinaryTree.TraversalMethods.Inorder;
}
if(!sep){
var sep=" ";
}
var s="";
switch(_26){
case dojo.collections.BinaryTree.TraversalMethods.Preorder:
s=preorderTraversal(_1a,sep);
break;
case dojo.collections.BinaryTree.TraversalMethods.Inorder:
s=inorderTraversal(_1a,sep);
break;
case dojo.collections.BinaryTree.TraversalMethods.Postorder:
s=postorderTraversal(_1a,sep);
break;
}
if(s.length==0){
return "";
}else{
return s.substring(0,s.length-sep.length);
}
};
this.count=0;
var _1a=this.root=null;
if(_1){
this.add(_1);
}
};
dojo.collections.BinaryTree.TraversalMethods={Preorder:1,Inorder:2,Postorder:3};
