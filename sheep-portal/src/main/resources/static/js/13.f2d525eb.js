(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[13],{"56b4":function(e,t,n){"use strict";n.r(t);var o=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"login-form flex-center"},[n("q-form",{ref:"loginForm",staticClass:"q-gutter-md",on:{submit:e.onSubmit,reset:e.onReset}},[n("q-input",{attrs:{filled:"",label:"用户名",hint:"您在本站的昵称",rules:[function(e){return e&&e.length>0||"请您务必输入一些东西"}]},model:{value:e.name,callback:function(t){e.name=t},expression:"name"}}),n("q-input",{attrs:{filled:"",type:"tel",label:"手机",hint:"您必须通过手机号验证才能成功注册",rules:e.phoneRules},scopedSlots:e._u([e.phone&&e.phone.length+""==="11"?{key:"after",fn:function(){return[n("q-btn",{attrs:{padding:"xs",disable:!e.show,flat:""}},[n("span",{directives:[{name:"show",rawName:"v-show",value:e.show,expression:"show"}],staticStyle:{"font-size":"medium"},on:{click:e.sendCode}},[e._v("获取验证码")]),n("span",{directives:[{name:"show",rawName:"v-show",value:!e.show,expression:"!show"}],staticStyle:{"font-size":"medium"}},[e._v(e._s(e.count)+"s后重新发送")])])]},proxy:!0}:null],null,!0),model:{value:e.phone,callback:function(t){e.phone=t},expression:"phone"}}),e.phone&&e.phone.length+""==="11"?n("q-input",{attrs:{filled:"",label:"验证码",rules:[function(e){return e&&e.length>0||"必须输入验证码"}],readonly:!e.sentCode},model:{value:e.code,callback:function(t){e.code=t},expression:"code"}}):e._e(),n("q-input",{attrs:{filled:"",type:"password",hint:"密码应当很难猜中",label:"密码"},model:{value:e.password,callback:function(t){e.password=t},expression:"password"}}),n("q-input",{attrs:{filled:"",type:e.isPwd?"password":"text",hint:"输入两次确保自己不会忘记",label:"确认密码","lazy-rules":"",rules:[function(e){return e&&e.length>0||"请务必确认密码"},function(t){return t&&t===e.password||"两次密码要一致"}]},scopedSlots:e._u([{key:"append",fn:function(){return[n("q-icon",{staticClass:"cursor-pointer",attrs:{name:e.isPwd?"visibility_off":"visibility"},on:{click:function(t){e.isPwd=!e.isPwd}}})]},proxy:!0}]),model:{value:e.pwdt,callback:function(t){e.pwdt=t},expression:"pwdt"}}),n("div",{staticClass:"row justify-end"},[n("div",{},[n("q-btn",{attrs:{label:"注册",type:"submit",color:"dark",unelevated:""},on:{click:function(t){return e.onSubmit()}}})],1),n("div",{staticClass:"offset-4"},[n("q-btn",{attrs:{label:"去登录",dense:"",unelevated:""},on:{click:function(t){return e.toLogin()}}})],1)])],1)],1)},s=[],i=(n("b0c0"),n("4ec3"),{name:"Register",data:function(){var e=/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{6,16}$/;return{name:null,phone:null,password:null,code:null,sentCode:!1,phoneRules:[function(e){return e&&e.length>0||"请务必输入手机号"},function(e){return e&&11==e.length||"请输入11位手机号码"}],pwdRules:[function(e){return e&&e.length>0||"请务必输入密码"},function(e){return e&&e.length>=6&&e.length<=18||"密码长度在6-18字符之间"},function(t){return t&&e.test(t)||"密码必须包含大写字母、小写字母、数字"}],pwdt:null,isPwd:!0,checkCode:null,show:!0,count:"",timer:null}},mounted:function(){console.log(this.$q.version)},methods:{sendCode:function(){var e=this,t=60;this.timer||(this.count=t,this.show=!1,this.timer=setInterval((function(){e.count>0&&e.count<=t?e.count--:(e.show=!0,clearInterval(e.timer),e.timer=null)}),1e3)),this.sentCode=!0,this.$axios.post("/code/"+this.phone).then((function(t){200==t.type&&(e.checkCode=t.data,console.log("checkCode:"+e.checkCode))})).catch((function(e){console.log(e)}))},onSubmit:function(){var e=this;this.$axios.post("/register",{username:this.name,password:this.password,mobile:this.phone,code:this.code}).then((function(t){200==t.status&&200==t.data.type&&(e.$q.notify({color:"positive",textColor:"white",icon:"cloud_done",message:"注册成功！"}),e.toLogin())})).catch((function(e){console.log(e)}))},onReset:function(){this.name=null,this.phone=null,this.password=null,this.pwdt=null,this.code=!1},toLogin:function(){this.$router.push({name:"Login"})}}}),l=i,a=n("2877"),u=n("0378"),c=n("27f9"),r=n("9c40"),d=n("0016"),h=n("eebe"),p=n.n(h),f=Object(a["a"])(l,o,s,!1,null,"337d5ddb",null);t["default"]=f.exports;p()(f,"components",{QForm:u["a"],QInput:c["a"],QBtn:r["a"],QIcon:d["a"]})}}]);