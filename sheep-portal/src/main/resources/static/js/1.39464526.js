(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[1],{"2da0":function(t,e,a){"use strict";a("5926")},5926:function(t,e,a){},"713b":function(t,e,a){"use strict";a.r(e);var o=function(){var t=this,e=t.$createElement,o=t._self._c||e;return o("q-layout",{staticStyle:{overflow:"hidden"},attrs:{view:"hHh lpr fFf"}},[o("q-header",{staticClass:"bg-primary",attrs:{"height-hint":"100"}},[o("div",{staticClass:"row no-wrap"},[o("q-toolbar",{staticClass:"col bg-primary"},[o("q-toolbar-title",{staticClass:"row items-center"},[o("my-head",{staticClass:"cursor-pointer",attrs:{head:a("e625")},on:{click:t.toMain}}),o("span",{staticClass:"cursor-pointer",on:{click:function(e){return t.toMain()}}},[t._v("ScholarSheep")])],1)],1),"main"!==t.path?o("search-tool"):t._e(),o("q-toolbar",{staticClass:"col bg-primary text-white q-gutter-x-md"},[o("q-space"),t.isLogin?t._e():o("div",[o("q-btn",{attrs:{flat:"",unelevated:""},on:{click:function(e){return t.toLogin()}}},[t._v("加入")])],1),o("div",{staticClass:"cursor-pointer",on:{click:function(e){return t.toUser()}}},[o("my-head",{attrs:{head:t.head,size:"36px"}})],1),o("q-btn",{attrs:{flat:"",round:"",icon:"more_vert"}},[o("q-menu",{attrs:{"auto-close":""}},[o("q-list",{staticStyle:{"min-width":"100px"},attrs:{separator:""}},[o("q-item",{attrs:{clickable:""}},[t.isLogin?o("q-item-section",{on:{click:t.logOut}},[t._v("退出登录")]):o("q-item-section",{on:{click:t.toLogin}},[t._v("登录")])],1),o("q-item",{attrs:{clickable:""},on:{click:t.handleExpert}},[o("q-item-section",[t._v("进入我的门户")])],1)],1)],1)],1)],1)],1)]),o("apply-form",{attrs:{show:t.applyForm},on:{closeForm:t.closeForm}}),o("q-page-container",{staticStyle:{height:"100vh"}},[o("q-scroll-area",{staticStyle:{width:"100%",height:"100%"}},[o("router-view")],1)],1),o("q-footer",{staticClass:"bg-primary text-white "},[o("q-toolbar",[o("q-toolbar-title",{staticClass:"row items-center"},[o("q-avatar",[o("img",{attrs:{src:a("e625")}})]),t._v("\n        ScholarSheep\n      ")],1),t._v("\n        绵羊学术——帮助你获得奖学金\n    ")],1)],1)],1)},i=[],n=a("4ec3"),r={name:"mainLayout",data:function(){return{uId:1,head:"",applyForm:!1}},computed:{path:function(){return"/"===this.$route.path?"main":"other"},isLogin:function(){return console.log("isLogin"+(null!==localStorage.getItem("userId"))),localStorage.getItem("userId")}},mounted:function(){console.log(this.applyForm),this.init()},watch:{head:{handler:function(t,e){t!==e&&this.init()},deep:!0,immediate:!0}},methods:{init:function(){var t=this;this.uId=localStorage.getItem("userId"),this.uId&&Object(n["t"])(this.uId).then((function(e){200===e.status&&(console.log("init"+e.data),t.head="https://cdn.quasar.dev/img/avatar.png")}))},toUser:function(){console.log("toUser"+this.uId),localStorage.getItem("userId")?this.$router.push({name:"User",params:{uId:this.uId}}):this.$q.notify({color:"negative",textColor:"white",icon:"warning",message:"请先登录"})},toMain:function(){this.$router.push({name:"Main"})},toLogin:function(){this.$router.push({name:"Login"})},logOut:function(){localStorage.removeItem("token"),localStorage.removeItem("userId"),console.log("logout"+localStorage.getItem("userId")+localStorage.getItem("token")),this.$q.notify("退出登录"),this.init(),this.$router.push({name:"Main"}),this.head="",window.location.reload(!0)},handleExpert:function(){var t=this;Object(n["u"])().then((function(e){200===e.status&&(200===e.data.type?t.$router.push({name:"expMain",params:{expId:"123"}}):4003===e.data.type&&(t.applyForm=!0))})).catch((function(t){console.log(t)}))},closeForm:function(t){this.applyForm=t}}},s=r,c=(a("2da0"),a("c701")),l=a("4d5a"),u=a("e359"),h=a("65c6"),m=a("6ac5"),p=a("2c91"),d=a("9c40"),g=a("4e73"),f=a("1c1c"),v=a("66e5"),q=a("4074"),I=a("09e3"),b=a("4983"),w=a("7ff0"),y=a("cb32"),S=a("8572"),Q=a("3117"),k=a.n(Q),x=Object(c["a"])(s,o,i,!1,null,null,null);e["default"]=x.exports;k()(x,"components",{QLayout:l["a"],QHeader:u["a"],QToolbar:h["a"],QToolbarTitle:m["a"],QSpace:p["a"],QBtn:d["a"],QMenu:g["a"],QList:f["a"],QItem:v["a"],QItemSection:q["a"],QPageContainer:I["a"],QScrollArea:b["a"],QFooter:w["a"],QAvatar:y["a"],QField:S["a"]})},e625:function(t,e,a){t.exports=a.p+"img/logo-square.3b6a0aa6.png"}}]);