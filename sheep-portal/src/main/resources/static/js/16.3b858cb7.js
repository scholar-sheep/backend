(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[16],{"30d6":function(t,e,n){"use strict";n.r(e);var o=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("q-page",{staticClass:"fit column content-center wrap",attrs:{padding:""}},[n("q-table",{staticStyle:{width:"90%"},attrs:{grid:t.$q.screen.xs,title:"收藏",data:t.data,columns:t.columns,"row-key":"name",filter:t.filter,separator:"cell",selection:t.selection,selected:t.selected,pagination:t.initialPagination,"visible-columns":t.visibleColumns,flat:""},on:{"update:selected":function(e){t.selected=e}},scopedSlots:t._u([{key:"top",fn:function(){return[t.me?n("q-btn",{attrs:{color:"primary",label:"批量关注",unelevated:""},on:{click:t.doAdd}}):t._e(),t.me?n("q-btn",{staticClass:"q-ml-sm",attrs:{color:"negative",label:"批量取消关注",unelevated:""},on:{click:t.doRemove}}):t._e(),n("q-space"),n("q-input",{attrs:{borderless:"",dense:"",debounce:"300",placeholder:"搜索关注列表"},scopedSlots:t._u([{key:"append",fn:function(){return[n("q-icon",{attrs:{name:"search"}})]},proxy:!0}]),model:{value:t.filter,callback:function(e){t.filter=e},expression:"filter"}})]},proxy:!0},{key:"body-cell-名字",fn:function(e){return[n("q-td",{attrs:{props:e}},[n("span",{staticStyle:{cursor:"pointer"},on:{click:function(n){return t.toExpert(e.row.id)}}},[t._v(t._s(e.row.name))])])]}},{key:"body-cell-关注",fn:function(e){return[n("q-td",{attrs:{props:e}},[n("q-icon",{staticStyle:{cursor:"pointer"},attrs:{name:e.row.icon,size:"large"},on:{click:function(n){return t.changeFollow(e.row)}}})],1)]}}])})],1)},i=[],a=(n("4160"),n("b0c0"),n("159b"),n("4ec3")),l={name:"Collection",data:function(){return{filter:"",selected:[],initialPagination:{sortBy:"desc",descending:!1,page:1,rowsPerPage:10},columns:[{name:"名字",required:!0,label:"学者",align:"center",field:function(t){return t.name},sortable:!0},{name:"所属机构",label:"所属机构",field:"org",align:"center"},{name:"成果数",label:"成果数",field:"n_pubs",align:"center",sortable:!0},{name:"被引量",label:"被引量",field:"n_citation",align:"center",sortable:!0},{name:"关注",label:"关注",field:"icon",align:"center"}],data:[]}},computed:{visibleColumns:function(){return this.me?["名字","所属机构","成果数","被引量","关注"]:["名字","所属机构","成果数","被引量"]},me:function(){return this.$route.params.uId+""===localStorage.getItem("userId")+""},selection:function(){return this.me?"multiple":"none"}},mounted:function(){this.init()},methods:{init:function(){var t=this;Object(a["n"])(this.$route.params.uId).then((function(e){200===e.status&&(e.data.data.forEach((function(e){var n=e;n.icon="o_visibility_off",t.data.push(n)})),t.initialPagination.rowsNumber=t.data.length,console.log(t.initialPagination.rowsNumber))})).catch((function(t){console.log(t)}))},doAdd:function(){var t=this;this.selected.forEach((function(e){t.doFollow(e,(function(t){t&&(e.icon="o_visibility_off")}))})),this.selected=[]},doRemove:function(){var t=this;this.selected.forEach((function(e){t.undoFollow(e,(function(t){t&&(e.icon="o_visibility")}))})),this.selected=[]},changeFollow:function(t){var e=this;this.data.forEach((function(n){n.id===t.id&&("o_visibility_off"===n.icon?(console.log("取关"),e.undoFollow(n,(function(t){t&&(n.icon="o_visibility",console.log(e.data,n,n.icon))}))):(console.log("关注"),e.doFollow(n,(function(t){t&&(n.icon="o_visibility_off",console.log(e.data,n,n.icon))}))))}))},toExpert:function(t){this.$router.push({name:"expMain",params:{expId:t}})},doFollow:function(t,e){Object(a["h"])(t.id).then((function(t){200===t.status?e(!0):e(!1)})).catch((function(t){console.log(t),e(!1)}))},undoFollow:function(t,e){Object(a["v"])(t.id).then((function(t){200===t.status?e(!0):e(!1)})).catch((function(t){console.log(t),e(!1)}))}}},c=l,s=n("2877"),r=n("9989"),u=n("eaac"),d=n("9c40"),f=n("2c91"),p=n("27f9"),m=n("0016"),b=n("db86"),h=n("eebe"),g=n.n(h),v=Object(s["a"])(c,o,i,!1,null,null,null);e["default"]=v.exports;g()(v,"components",{QPage:r["a"],QTable:u["a"],QBtn:d["a"],QSpace:f["a"],QInput:p["a"],QIcon:m["a"],QTd:b["a"]})}}]);