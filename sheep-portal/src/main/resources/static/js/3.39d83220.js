(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[3],{b6d7:function(t,e,o){"use strict";o.r(e);var a=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("q-layout",{staticStyle:{overflow:"hidden"},attrs:{view:"hHh lpr fFf"}},[a("q-header",{staticClass:"bg-primary",attrs:{"height-hint":"100"}},[a("div",{staticClass:"row no-wrap"},[a("q-toolbar",{staticClass:"col bg-primary"},[a("q-toolbar-title",{staticClass:"row items-center"},[a("my-head",{staticClass:"cursor-pointer",attrs:{head:o("e625")},on:{click:t.toMain}}),a("span",{staticClass:"cursor-pointer",on:{click:function(e){return t.toMain()}}},[t._v("ScholarSheep")])],1)],1),"main"!==t.path?a("search-tool"):t._e(),a("q-toolbar",{staticClass:"col bg-primary text-white q-gutter-x-md"},[a("q-space"),t.isLogin?t._e():a("div",[a("q-btn",{attrs:{flat:"",unelevated:""},on:{click:function(e){return t.toLogin()}}},[t._v("加入")])],1),a("div",{staticClass:"cursor-pointer",on:{click:function(e){return t.toUser()}}},[a("my-head",{attrs:{head:t.head,size:"36px"}})],1),a("q-btn",{attrs:{flat:"",round:"",icon:"more_vert"}},[a("q-menu",{attrs:{"auto-close":""}},[a("q-list",{staticStyle:{"min-width":"100px"},attrs:{separator:""}},[a("q-item",{attrs:{clickable:""}},[t.isLogin?a("q-item-section",{on:{click:t.logOut}},[t._v("退出登录")]):a("q-item-section",{on:{click:t.toLogin}},[t._v("登录")])],1),a("q-item",{attrs:{clickable:""},on:{click:t.handleExpert}},[a("q-item-section",[t._v("进入我的门户")])],1)],1)],1)],1)],1)],1)]),a("apply-form",{attrs:{show:t.applyForm},on:{closeForm:t.closeForm}}),a("q-page-container",{staticStyle:{height:"100vh"}},[a("q-scroll-area",{staticStyle:{width:"100%",height:"100%"}},[a("router-view")],1)],1),a("q-footer",{staticClass:"bg-primary text-white"},[a("q-toolbar",[a("q-toolbar-title",{staticClass:"row items-center"},[a("q-avatar",[a("img",{attrs:{src:o("e625")}})]),t._v("ScholarSheep\n      ")],1),t._v("--way to your Scholarship!\n    ")],1)],1)],1)},i=[],n=o("4ec3"),r={name:"mMainLayout",data:function(){return{uId:1,head:"",applyForm:!1}},computed:{path:function(){return"/"===this.$route.path?"main":"other"},isLogin:function(){return console.log("isLogin"+localStorage.getItem("userId")),localStorage.getItem("userId")}},mounted:function(){console.log(this.applyForm),this.init()},watch:{head:{handler:function(t,e){t!==e&&this.init()},deep:!0,immediate:!0}},methods:{init:function(){var t=this;this.uId=localStorage.getItem("userId"),console.log("init"+this.uId+(this.uId+""!==null)),this.uId+""!==null&&Object(n["t"])(this.uId).then((function(e){200===e.status&&(console.log("init"+e.data),t.head="https://cdn.quasar.dev/img/avatar.png")}))},toUser:function(){console.log("toUser"+this.uId),null!==localStorage.getItem("userId")?this.$router.push({name:"User",params:{uId:this.uId}}):this.$q.notify({color:"negative",textColor:"white",icon:"warning",message:"请先登录"})},toMain:function(){this.$router.push({name:"Main"})},toLogin:function(){this.$router.push({name:"Login"})},logOut:function(){localStorage.removeItem("token"),localStorage.removeItem("userId"),console.log("logout"+localStorage.getItem("userId")+localStorage.getItem("token")),this.$q.notify("退出登录"),this.init(),this.$router.push({name:"Main"}),this.head="",window.location.reload(!0)},handleExpert:function(){var t=this;Object(n["u"])().then((function(e){200===e.status?t.$router.push({name:"expMain",params:{expId:"123"}}):403===e.status&&(t.applyForm=!0)})).catch((function(t){console.log(t)}))},closeForm:function(t){this.applyForm=t}}},s=r,c=(o("e7ce"),o("c701")),l=o("4d5a"),u=o("e359"),h=o("65c6"),m=o("6ac5"),d=o("2c91"),p=o("9c40"),g=o("4e73"),f=o("1c1c"),v=o("66e5"),q=o("4074"),I=o("09e3"),w=o("4983"),b=o("7ff0"),y=o("cb32"),S=o("8572"),Q=o("3117"),k=o.n(Q),x=Object(c["a"])(s,a,i,!1,null,null,null);e["default"]=x.exports;k()(x,"components",{QLayout:l["a"],QHeader:u["a"],QToolbar:h["a"],QToolbarTitle:m["a"],QSpace:d["a"],QBtn:p["a"],QMenu:g["a"],QList:f["a"],QItem:v["a"],QItemSection:q["a"],QPageContainer:I["a"],QScrollArea:w["a"],QFooter:b["a"],QAvatar:y["a"],QField:S["a"]})},cd8c:function(t,e,o){},e625:function(t,e,o){t.exports=o.p+"img/logo-square.3b6a0aa6.png"},e7ce:function(t,e,o){"use strict";o("cd8c")}}]);