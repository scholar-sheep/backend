(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[1],{"2da0":function(t,a,e){"use strict";e("5926")},5926:function(t,a,e){},"713b":function(t,a,e){"use strict";e.r(a);var o=function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("q-layout",{staticStyle:{overflow:"hidden"},attrs:{view:"hHh lpr fFf"}},[e("q-header",{staticClass:"bg-grey-8",attrs:{"height-hint":"100"}},[e("div",{staticClass:"row no-wrap"},[e("q-toolbar",{staticClass:"col bg-grey-8"},[e("q-toolbar-title",{staticClass:"row items-center"},[e("my-head",{staticClass:"cursor-pointer",attrs:{icon:"school"},on:{click:t.toMain}}),e("span",{staticClass:"cursor-pointer",on:{click:function(a){return t.toMain()}}},[t._v("ScholarSheep")])],1)],1),"main"!==t.path?e("search-tool"):t._e(),e("q-toolbar",{staticClass:"col bg-grey-8 text-white"},[e("q-space"),t.isLogin?t._e():e("div",[e("q-btn",{attrs:{flat:"",unelevated:""},on:{click:function(a){return t.toLogin()}}},[t._v("加入")])],1),e("div",{staticClass:"cursor-pointer",on:{click:function(a){return t.toUser()}}},[e("my-head",{attrs:{head:t.head,size:"36px"}})],1),e("q-btn",{attrs:{flat:"",round:"",icon:"more_vert"}})],1)],1)]),e("q-page-container",{staticStyle:{height:"100vh"}},[e("q-scroll-area",{staticStyle:{width:"100%",height:"100%"}},[e("router-view")],1)],1),e("q-footer",{staticClass:"bg-grey-8 text-white"},[e("q-toolbar",[e("q-toolbar-title",[e("q-avatar",[e("img",{attrs:{src:"https://cdn.quasar.dev/logo/svg/quasar-logo.svg"}})]),t._v("页脚\n      ")],1)],1)],1)],1)},n=[],i=e("4ec3"),r={name:"mainLayout",data:function(){return{uId:1,head:""}},computed:{path:function(){return"/"===this.$route.path?"main":"other"},isLogin:function(){return!1}},mounted:function(){this.init()},methods:{init:function(){var t=this;this.uId=this.$route.params.userId,Object(i["i"])(this.uId).then((function(a){0===a.status&&(t.head=a.data.avatar)}))},toUser:function(){this.$router.push({name:"User",params:{uId:this.uId}})},toMain:function(){this.$router.push({name:"Main"})},toLogin:function(){this.$router.push({name:"Login"})}}},s=r,c=(e("2da0"),e("c701")),u=e("4d5a"),l=e("e359"),h=e("65c6"),d=e("6ac5"),p=e("2c91"),f=e("9c40"),g=e("09e3"),v=e("4983"),b=e("7ff0"),m=e("cb32"),q=e("8572"),w=e("3117"),y=e.n(w),Q=Object(c["a"])(s,o,n,!1,null,null,null);a["default"]=Q.exports;y()(Q,"components",{QLayout:u["a"],QHeader:l["a"],QToolbar:h["a"],QToolbarTitle:d["a"],QSpace:p["a"],QBtn:f["a"],QPageContainer:g["a"],QScrollArea:v["a"],QFooter:b["a"],QAvatar:m["a"],QField:q["a"]})}}]);