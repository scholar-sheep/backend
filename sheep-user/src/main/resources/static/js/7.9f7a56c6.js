(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[7],{"255cf":function(t,e,a){"use strict";a.r(e);var r=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("q-page",{staticClass:"column"},[a("div",{staticClass:"row col",staticStyle:{height:"50%"}},[a("div",{staticClass:"col-9 message"},[a("q-card",{staticClass:"my-card",attrs:{dark:"",bordered:""}},[a("q-card-section",[a("div",{staticClass:"row"},[a("div",{staticClass:"col-2",staticStyle:{"text-align":"center"}},[a("q-avatar",{attrs:{size:"80px"}},[a("img",{attrs:{src:"https://cdn.quasar.dev/img/avatar.png"}})])],1),a("div",{staticClass:"col-8"},[a("div",{staticClass:"text-h4"},[t._v(t._s(t.info["姓名"]))]),a("div",{staticClass:"text-subtitle1"},[t._v(t._s(t.info["职称"]))])]),a("div",{staticClass:"col-2"},[a("p",[t.followed||t.owner?t._e():a("q-btn",{attrs:{color:"primary",icon:"playlist_add",label:"关注",size:"md"},on:{click:function(e){return t.follow()}}}),t.followed?a("q-btn",{attrs:{color:"primary",icon:"playlist_add",label:"取消关注",size:"md"},on:{click:function(e){return t.unFollow()}}}):t._e()],1)])])]),a("q-separator",{attrs:{dark:"",inset:""}}),a("q-card-section",[a("div",[a("q-list",{attrs:{dark:"",separator:""}},[a("q-item",{directives:[{name:"ripple",rawName:"v-ripple"}],attrs:{clickable:""}},[a("q-item-section",{on:{click:function(e){return t.toOrg()}}},[a("q-item-label",{staticClass:"text-subtitle1"},[t._v("单位")]),a("q-item-label",{attrs:{caption:""}},[a("p",[t._v(t._s(t.info["机构"]))])])],1)],1),a("q-item",{directives:[{name:"ripple",rawName:"v-ripple"}],attrs:{clickable:""}},[a("q-item-section",[a("q-item-label",{staticClass:"text-subtitle1"},[t._v("领域")]),a("q-item-label",{attrs:{caption:""}},[a("p",[t._v(t._s(t.info["领域"]))])])],1)],1)],1)],1)])],1)],1),a("div",{staticClass:"col-3 data"},[a("q-card",{staticClass:"my-card",attrs:{bordered:""}},[a("q-card-section",{staticClass:"items-center"},[a("div",{staticClass:"text-center"},[a("a-statistic",{attrs:{title:"共有成果",value:t.info.成果数,suffix:"篇",valueStyle:"{font-size:'30px'}"}}),a("a-statistic",{attrs:{title:"总被引",value:t.info["引用数"],suffix:"次"}}),a("a-statistic",{attrs:{title:"共有",value:t.info["关注人数"],suffix:"人关注了ta"}})],1)])],1)],1)]),a("div",{staticClass:"row col"},[a("div",{staticClass:"col-6 contents"},[a("q-card",{staticClass:"my-card",attrs:{bordered:""}},[a("q-card-section",[a("div",{staticClass:"row"},[a("div",{staticClass:"text-h6 col-10"},[t._v("论文列表")]),a("div",{staticClass:"col-2"},[a("p",[t.owner?a("q-btn",{attrs:{color:"primary",icon:"create",label:"编辑",size:"md"},on:{click:function(e){t.edit=!0}}}):t._e(),t.owner?t._e():a("q-btn",{attrs:{color:"primary",icon:"create",label:"认领",size:"md"},on:{click:function(e){return t.apply()}}}),a("admin-form",{attrs:{edit:t.edit,papers:t.papers,expId:this.expId},on:{closeDialog:t.closeDialog}})],1)])])]),a("q-separator",{attrs:{inset:""}}),a("q-card-section",[a("div",[a("q-list",{attrs:{separator:""}},[a("q-scroll-area",{staticStyle:{height:"300px"}},t._l(t.papers,(function(e){return a("q-item",{directives:[{name:"ripple",rawName:"v-ripple"}],key:e.id,attrs:{clickable:""}},[a("q-item-section",{on:{click:function(a){return t.toPaper(e.id)}}},[t._v(t._s(e.name))])],1)})),1)],1)],1)])],1)],1),a("div",{staticClass:"col-6 network"},[a("q-card",{staticClass:"my-card",attrs:{dark:"",bordered:""}},[a("q-card-section",[a("div",{staticStyle:{width:"100%",height:"400px"},attrs:{id:"myChart"}})])],1)],1)])])},o=[],n=(a("b0c0"),a("d3b7"),a("25f0"),a("9523")),s=a.n(n),i=a("4ec3"),l={name:"hello",data:function(){return{msg:"Welcome to Your Vue.js App",followed:!1,owner:!1,edit:!1,expId:"",info:{"姓名":"Scholar Name","职称":"Professor","机构":"Organization Name","领域":"blablablablablablabla","成果数":0,"引用数":0,"关注人数":0},papers:[{id:1,name:"我是一篇假论文",authorNames:"假的，假的，假的",n_citation:10086,year:2e3,venue:"编的"},{id:2,name:"我也是一篇假论文",authorNames:"假的，假的，假的",n_citation:10086,year:2e3,venue:"编的"}]}},mounted:function(){this.drawLine(),this.init()},methods:{init:function(){var t=this;this.expId=this.$route.params.expId,console.log(this.$route.params,"------",this.expId),Object(i["f"])(this.expId).then((function(e){console.log(e.data),200===e.status&&(t.info.姓名=e.data.data.name,t.info.职称=e.data.data.position,t.info.机构=e.data.data.org,t.info.成果数=e.data.data.n_pubs,t.info.引用数=e.data.data.n_citation,t.info.领域=e.data.data.tags_t,t.papers=e.paperList)})),Object(i["i"])(this.expId).then((function(e){200===e.status&&("认领关系"===e.data.data?t.owner=!0:"关注关系"===e.data.data?t.followed=!0:"没关系"===e.data.data&&(t.owner=!1,t.followed=!1))})),Object(i["g"])(this.expId).then((function(e){200===e.status?t.info.关注人数=e.data.data:console.log("获取关注人数失败！")}))},toOrg:function(){this.$router.push({name:"orgMain",params:{orgId:1}})},toPaper:function(t){this.$router.push({name:"Academic",params:{acaId:t}})},follow:function(){var t=this;Object(i["c"])(this.expId).then((function(e){200===e.status&&(t.$q.notify("关注成功"),t.followed=!0)}))},unFollow:function(){var t=this;this.followed=!1,Object(i["m"])(this.expId).then((function(e){200===e.status&&(t.$q.notify("取消关注成功"),t.followed=!0)}))},apply:function(t){function e(){return t.apply(this,arguments)}return e.toString=function(){return t.toString()},e}((function(){var t=this;this.$q.notify("认领成功"),this.owner=!0,apply(this.expId).then((function(e){200===e.status&&(t.$q.notify("认领成功"),t.owner=!0)}))})),closeDialog:function(t){this.edit=t},drawLine:function(){var t,e,a=this.$echarts.init(document.getElementById("myChart"),"dark");a.setOption((e={backgroundColor:"#3A4A56",title:{text:"关系图谱"},tooltip:{formatter:function(t){return t.data.name}},toolbox:{show:!0,feature:{magicType:["line","bar"],restore:!0,saveAsImage:!0}},legend:[{x:"left",data:["调研机构","接待公司"]}],lineStyle:{normal:{show:!0,color:"target",curveness:.3}},label:{normal:{show:!0,position:"right",formatter:function(t){return t.data.label}}},force:{repulsion:100,gravity:.03,edgeLength:80,layoutAnimation:!0},edgeLabel:{normal:{show:!0,formatter:function(t){return t.data.name}}}},s()(e,"edgeLabel",{normal:{textStyle:{fontSize:20}}}),s()(e,"series",[(t={type:"graph",layout:"force",symbolSize:80,roam:!0,edgeSymbol:["circle","arrow"],edgeSymbolSize:[4,10],edgeLabel:{normal:{textStyle:{fontSize:20}}},force:{repulsion:2500,edgeLength:[10,50]},draggable:!1,itemStyle:{normal:{color:"#4b565b"}},lineStyle:{normal:{width:2,color:"#4b565b"}}},s()(t,"edgeLabel",{normal:{show:!0,formatter:function(t){return t.data.name}}}),s()(t,"label",{normal:{show:!0,textStyle:{}}}),s()(t,"data",[{name:"侯亮平",des:"最高检反贪局侦查处处长，汉东省人民检察院副检察长兼反贪局局长。<br/>经过与腐败违法分子的斗争，最终将一批腐败分子送上了审判台，<br/>正义战胜邪恶，自己也迎来了成长。",symbolSize:100,itemStyle:{normal:{color:"red"}}},{name:"李达康",des:"汉东省省委常委，京州市市委书记。是一个正义无私的好官。<br/>但为人过于爱惜自己的羽毛，对待身边的亲人和朋友显得有些无情。",itemStyle:{normal:{color:"red"}}},{name:"祁同伟",des:"汉东省公安厅厅长",symbolSize:100},{name:"陈岩石",des:"离休干部、汉东省检察院前常务副检察长。充满正义，平凡而普通的共 产 党人。<br/>对大老虎赵立春，以各种形式执着举报了十二年。<br/>在这场关系党和国家生死存亡的斗争中，老人家以耄耋高龄，<br/>义无反顾地背起了炸 药包，在反腐胜利前夕因病去世。",itemStyle:{normal:{color:"red"}}},{name:"陆亦可",des:"汉东省检察院反贪局一处处长",itemStyle:{normal:{color:"red"}}},{name:"高育良",des:"汉东省省委副书记兼政法委书记。年近六十，是一个擅长太极功夫的官场老手。侯亮平、陈海和祁同伟是其学生。",symbolSize:100},{name:"沙瑞金",des:"汉东省省委书记",itemStyle:{normal:{color:"red"}}},{name:"高小琴",des:"山水集团董事长，是一位叱咤于政界和商界的风云人物，处事圆滑、精明干练。"},{name:"高小凤"},{name:"赵东来",des:"汉东省京州市公安局局长，充满正义，整治恶势力，<br/>在与检察部门的合作中从最初的质疑到之后的通力配合。",itemStyle:{normal:{color:"red"}}},{name:"程度",des:"汉东省京州市公安局光明区分局局长，因犯错误，被李达康书记和赵东来局长点名要清除公安队伍。<br/>但在高小琴的帮助下，祁同伟调他当上了省公安厅办公室副主任。<br/>尽管程度逃避了所有罪责，上面也有人保他，<br/>但最终还是在反贪局局长侯亮平的缜密侦查下被绳之于法。"},{name:"吴惠芬",des:"汉东省省委副书记高育良的妻子，汉东大学明史教授。",itemStyle:{normal:{color:"red"}}},{name:"赵瑞龙",des:"副国级人物赵立春的公子哥，官二代，打着老子的旗子，<br/>黑白两道通吃，可谓呼风唤雨，权倾一时。"},{name:"赵立春",des:"副国级领导人，曾经的改革闯将，在权利面前，显得极其渺小。"},{name:"陈海",itemStyle:{normal:{color:"red"}}},{name:"梁璐",itemStyle:{normal:{color:"red"}}},{name:"刘新建"},{name:"欧阳菁"},{name:"吴心怡",itemStyle:{normal:{color:"red"}}},{name:"蔡成功"},{name:"丁义珍"}]),s()(t,"links",[{source:"高育良",target:"侯亮平",name:"师生",des:"侯亮平是高育良的得意门生"},{source:"高育良",target:"祁同伟",name:"师生"},{source:"赵立春",target:"高育良",name:"前领导"},{source:"赵立春",target:"赵瑞龙",name:"父子"},{source:"赵立春",target:"刘新建",name:"前部队下属"},{source:"李达康",target:"赵立春",name:"前任秘书"},{source:"祁同伟",target:"高小琴",name:"情人"},{source:"陈岩石",target:"陈海",name:"父子"},{source:"陆亦可",target:"陈海",name:"属下"},{source:"沙瑞金",target:"陈岩石",name:"故人"},{source:"祁同伟",target:"陈海",name:"师哥"},{source:"祁同伟",target:"侯亮平",name:"师哥"},{source:"侯亮平",target:"陈海",name:"同学，生死朋友"},{source:"高育良",target:"吴惠芬",name:"夫妻"},{source:"陈海",target:"赵东来",name:"朋友"},{source:"高小琴",target:"高小凤",name:"双胞胎",symbolSize:"1"},{source:"赵东来",target:"陆亦可",name:"爱慕"},{source:"祁同伟",target:"程度",name:"领导"},{source:"高育良",target:"高小凤",name:"情人"},{source:"赵瑞龙",target:"祁同伟",name:"勾结",symbolSize:"1"},{source:"李达康",target:"赵东来",name:"领导"},{source:"赵瑞龙",target:"程度",name:"领导"},{source:"沙瑞金",target:"李达康",name:"领导"},{source:"沙瑞金",target:"高育良",name:"领导"},{source:"祁同伟",target:"梁璐",name:"夫妻"},{source:"吴惠芬",target:"梁璐",name:"朋友"},{source:"李达康",target:"欧阳菁",name:"夫妻"},{source:"赵瑞龙",target:"刘新建",name:"洗钱工具"},{source:"赵瑞龙",target:"高小琴",name:"勾结，腐化",symbolSize:"1"},{source:"赵瑞龙",target:"高小凤",name:"腐化"},{source:"吴心怡",target:"陆亦可",name:"母女"},{source:"吴心怡",target:"吴惠芬",name:"姐妹"},{source:"蔡成功",target:"侯亮平",name:"发小"},{source:"蔡成功",target:"欧阳菁",name:"举报",lineStyle:{normal:{type:"dotted",color:"#000"}}},{source:"欧阳菁",target:"刘新建",name:"举报",lineStyle:{normal:{type:"dotted",color:"#000"}}},{source:"刘新建",target:"赵瑞龙",name:"举报",lineStyle:{normal:{type:"dotted",color:"#000"}}},{source:"李达康",target:"丁义珍",name:"领导"},{source:"高小琴",target:"丁义珍",name:"策划出逃"},{source:"祁同伟",target:"丁义珍",name:"勾结"},{source:"赵瑞龙",target:"丁义珍",name:"勾结"}]),t)]),e))}}},c=l,m=(a("26b8"),a("2877")),d=a("9989"),u=a("f09f"),p=a("a370"),f=a("cb32"),g=a("9c40"),b=a("eb85"),y=a("1c1c"),h=a("66e5"),v=a("4074"),w=a("0170"),S=a("4983"),q=a("714f"),x=a("eebe"),C=a.n(x),_=Object(m["a"])(c,r,o,!1,null,"6916caae",null);e["default"]=_.exports;C()(_,"components",{QPage:d["a"],QCard:u["a"],QCardSection:p["a"],QAvatar:f["a"],QBtn:g["a"],QSeparator:b["a"],QList:y["a"],QItem:h["a"],QItemSection:v["a"],QItemLabel:w["a"],QScrollArea:S["a"]}),C()(_,"directives",{Ripple:q["a"]})},"26b8":function(t,e,a){"use strict";var r=a("78fe"),o=a.n(r);o.a},"78fe":function(t,e,a){}}]);