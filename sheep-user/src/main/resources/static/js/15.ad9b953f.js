(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[15],{"30d6":function(e,i,t){"use strict";t.r(i);var n=function(){var e=this,i=e.$createElement,t=e._self._c||i;return t("q-page",{staticClass:"fit column content-center wrap",attrs:{padding:""}},[t("q-table",{staticStyle:{width:"90%"},attrs:{grid:e.$q.screen.xs,title:"收藏",data:e.data,columns:e.columns,"row-key":"name",filter:e.filter,separator:"cell",selection:e.selection,selected:e.selected,pagination:e.initialPagination,"visible-columns":e.visibleColumns,flat:""},on:{"update:selected":function(i){e.selected=i}},scopedSlots:e._u([{key:"top",fn:function(){return[e.me?t("q-btn",{attrs:{color:"primary",label:"批量关注",unelevated:""},on:{click:e.doAdd}}):e._e(),e.me?t("q-btn",{staticClass:"q-ml-sm",attrs:{color:"negative",label:"批量取消关注",unelevated:""},on:{click:e.doRemove}}):e._e(),t("q-space"),t("q-input",{attrs:{borderless:"",dense:"",debounce:"300",placeholder:"搜索关注列表"},scopedSlots:e._u([{key:"append",fn:function(){return[t("q-icon",{attrs:{name:"search"}})]},proxy:!0}]),model:{value:e.filter,callback:function(i){e.filter=i},expression:"filter"}})]},proxy:!0},{key:"body-cell-名字",fn:function(i){return[t("q-td",{attrs:{props:i}},[t("span",{staticStyle:{cursor:"pointer"},on:{click:function(t){return e.toExpert(i.row)}}},[e._v(e._s(i.row.name))])])]}},{key:"body-cell-关注",fn:function(i){return[t("q-td",{attrs:{props:i}},[t("q-icon",{staticStyle:{cursor:"pointer"},attrs:{name:i.row.icon,size:"large"},on:{click:function(t){return e.changeFollow(i.row)}}})],1)]}}])})],1)},o=[],a=(t("4160"),t("b0c0"),t("159b"),{name:"Collection",data:function(){return{filter:"",selected:[],initialPagination:{sortBy:"desc",descending:!1,page:1,rowsPerPage:10},columns:[{name:"名字",required:!0,label:"学者",align:"center",field:function(e){return e.name},format:function(e){return"".concat(e)},sortable:!0},{name:"所属机构",label:"所属机构",field:"fat",align:"center"},{name:"成果数",label:"成果数",field:"carbs",align:"center",sortable:!0},{name:"关注",label:"关注",field:"fat",align:"center"}],data:[{name:"Frozen Yogurt",icon:"o_visibility_off",calories:159,fat:6,carbs:24},{name:"Ice cream sandwich",icon:"o_visibility_off",calories:237,fat:9,carbs:37},{name:"Eclair",icon:"o_visibility_off",calories:262,fat:16,carbs:23},{name:"Cupcake",icon:"o_visibility_off",calories:305,fat:3.7,carbs:67},{name:"Gingerbread",icon:"o_visibility_off",calories:356,fat:16,carbs:49},{name:"Jelly bean",icon:"o_visibility_off",calories:375,fat:0,carbs:94},{name:"Lollipop",icon:"o_visibility_off",calories:392,fat:.2,carbs:98},{name:"Honeycomb",icon:"o_visibility_off",calories:408,fat:3.2,carbs:87},{name:"Donut",icon:"o_visibility_off",calories:452,fat:25,carbs:51},{name:"KitKat",icon:"o_visibility_off",calories:518,fat:26,carbs:65}]}},computed:{visibleColumns:function(){return this.me?["名字","所属机构","成果数","关注"]:["名字","所属机构","成果数"]},me:function(){return this.$route.params.uId+""==="2"},selection:function(){return this.me?"multiple":"none"}},methods:{doAdd:function(){this.$q.notify("已关注"),this.selected.forEach((function(e){e.icon="o_visibility_off"})),this.selected=[]},doRemove:function(){this.$q.notify("已取消关注"),this.selected.forEach((function(e){e.icon="o_visibility"})),this.selected=[]},changeFollow:function(e){"o_visibility_off"===e.icon?e.icon="o_visibility":e.icon="o_visibility_off",this.$q.notify("取消关注"+e)},toExpert:function(e){this.$router.push({name:"expMain",params:{expId:e}})}}}),c=a,l=t("c701"),s=t("9989"),r=t("eaac"),f=t("9c40"),u=t("2c91"),d=t("27f9"),b=t("0016"),m=t("db86"),p=t("3117"),_=t.n(p),y=Object(l["a"])(c,n,o,!1,null,null,null);i["default"]=y.exports;_()(y,"components",{QPage:s["a"],QTable:r["a"],QBtn:f["a"],QSpace:u["a"],QInput:d["a"],QIcon:b["a"],QTd:m["a"]})}}]);