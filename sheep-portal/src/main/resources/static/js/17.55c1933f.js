(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[17],{"693e":function(t,e,a){"use strict";a.r(e);var s=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("q-page",{staticClass:"fit row justify-center wrap"},[a("div",{staticClass:"col q-pa-xl row full-width"},[a("div",{staticClass:"col"},t._l(t.menuList,(function(e,s){return a("q-list",{key:s},[a("q-item",[a("q-item-section",{attrs:{avatar:""}},[a("my-head",{attrs:{icon:"inbox"}})],1),a("q-item-section",[t._v("\n            "+t._s(e.label)+"\n          ")])],1)],1)})),1),a("q-separator",{attrs:{vertical:""}}),a("div",{staticClass:"col-9"},[a("q-tab-panels",{staticStyle:{height:"100%"},attrs:{animated:"",vertical:"","transition-prev":"jump-up","transition-next":"jump-up"},model:{value:t.tab,callback:function(e){t.tab=e},expression:"tab"}},t._l(t.menuList,(function(e,s){return a("q-tab-panel",{key:s,staticStyle:{position:"relative",padding:"0"},attrs:{name:e},on:{click:function(a){return t.getMessageList(e)}}},[a("div",{staticClass:"column",staticStyle:{width:"100%",height:"75%","background-color":"whitesmoke"}},[a("div",{staticStyle:{padding:"16px"}},t._l(t.messageList,(function(e,s){return a("q-chat-message",{key:s,attrs:{name:e.name,avatar:"https://cdn.quasar.dev/img/avatar1.jpg",text:e.message,stamp:t.mesasgeItem.time,sent:e.send,"bg-color":"positive"}})})),1)]),a("div",{staticStyle:{bottom:"0",position:"absolute",width:"100%"}},[a("q-editor",{attrs:{"max-height":"10rem",square:"",flat:"",definitions:{bold:{label:"Bold",icon:null,tip:"My bold tooltip"}}},model:{value:t.editor,callback:function(e){t.editor=e},expression:"editor"}})],1)])})),1)],1)],1)])},i=[],n=a("2fce"),o=a.n(n),l=(a("1762"),a("4ec3"),{name:"Messages",data:function(){var t;return t={tab:"Outbox",exp:"",menuList:menuList,editor:"这是编辑器"},o()(t,"menuList",[]),o()(t,"messageList",[]),t},mounted:function(){this.getMessageUser(),this.getMessageList(2)},methods:{searchExp:function(){this.$q.notify("搜索"+this.exp)},getMessageUser:function(){var t=this;this.$axios.get("/portal/peopleList",{},{headers:{XUserId:"1"}}).then((function(e){200===e.status&&(console.log("res status 200"),t.menuList=e.data.data,console.log(t.menuList))}))},getMessageList:function(t){var e=this;this.$axios.get("/portal/message",{params:{that_user_id:t}}).then((function(t){200===t.status&&(e.messageList=t.data.data,console.log(e.messageList))}))}}}),r=l,c=a("c701"),u=a("9989"),d=a("1c1c"),m=a("66e5"),p=a("27f9"),g=a("0016"),h=a("eb85"),b=a("4074"),f=a("adad"),v=a("823b"),L=a("8169"),x=a("d66b"),q=a("714f"),w=a("3117"),y=a.n(w),Q=Object(c["a"])(r,s,i,!1,null,null,null);e["default"]=Q.exports;y()(Q,"components",{QPage:u["a"],QList:d["a"],QItem:m["a"],QInput:p["a"],QIcon:g["a"],QSeparator:h["a"],QItemSection:b["a"],QTabPanels:f["a"],QTabPanel:v["a"],QChatMessage:L["a"],QEditor:x["a"]}),y()(Q,"directives",{Ripple:q["a"]})}}]);