(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[13],{"56b4":function(e,n,t){"use strict";t.r(n);var o=function(){var e=this,n=e.$createElement,t=e._self._c||n;return t("div",{staticClass:"login-form flex-center"},[t("q-form",{ref:"loginForm",staticClass:"q-gutter-md",on:{submit:e.onSubmit,reset:e.onReset}},[t("q-input",{attrs:{filled:"",label:"用户名",hint:"您在本站的昵称",rules:[function(e){return e&&e.length>0||"请您务必输入一些东西"}]},model:{value:e.name,callback:function(n){e.name=n},expression:"name"}}),t("q-input",{attrs:{filled:"",type:"tel",label:"手机",hint:"您必须通过手机号验证才能成功注册",rules:e.phoneRules},scopedSlots:e._u([e.phone&&e.phone.length+""==="11"?{key:"after",fn:function(){return[t("q-btn",{attrs:{padding:"xs",disable:!e.show,flat:""}},[t("span",{directives:[{name:"show",rawName:"v-show",value:e.show,expression:"show"}],staticStyle:{"font-size":"medium"},on:{click:e.sendCode}},[e._v("获取验证码")]),t("span",{directives:[{name:"show",rawName:"v-show",value:!e.show,expression:"!show"}],staticStyle:{"font-size":"medium"}},[e._v(e._s(e.count)+"s后重新发送")])])]},proxy:!0}:null],null,!0),model:{value:e.phone,callback:function(n){e.phone=n},expression:"phone"}}),e.phone&&e.phone.length+""==="11"?t("q-input",{attrs:{filled:"",label:"验证码",rules:[function(e){return e&&e.length>0||"必须输入验证码"}],readonly:!e.sentCode},model:{value:e.code,callback:function(n){e.code=n},expression:"code"}}):e._e(),t("q-input",{attrs:{filled:"",type:"password",hint:"密码应当很难猜中",label:"密码"},model:{value:e.password,callback:function(n){e.password=n},expression:"password"}}),t("q-input",{attrs:{filled:"",type:e.isPwd?"password":"text",hint:"输入两次确保自己不会忘记",label:"确认密码","lazy-rules":"",rules:[function(e){return e&&e.length>0||"请务必确认密码"},function(n){return n&&n===e.password||"两次密码要一致"}]},scopedSlots:e._u([{key:"append",fn:function(){return[t("q-icon",{staticClass:"cursor-pointer",attrs:{name:e.isPwd?"visibility_off":"visibility"},on:{click:function(n){e.isPwd=!e.isPwd}}})]},proxy:!0}]),model:{value:e.pwdt,callback:function(n){e.pwdt=n},expression:"pwdt"}}),t("div",{staticClass:"row justify-end"},[t("div",{},[t("q-btn",{attrs:{label:"注册",type:"submit",color:"dark",unelevated:""},on:{click:function(n){return e.onSubmit()}}})],1),t("div",{staticClass:"offset-4"},[t("q-btn",{attrs:{label:"去登录",dense:"",unelevated:""},on:{click:function(n){return e.toLogin()}}})],1)])],1)],1)},s=[],i=(t("b0c0"),t("4ec3"),{name:"Register",data:function(){var e=/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{6,16}$/;return{name:null,phone:null,password:null,code:null,sentCode:!1,phoneRules:[function(e){return e&&e.length>0||"请务必输入手机号"},function(e){return e&&11==e.length||"请输入11位手机号码"}],pwdRules:[function(e){return e&&e.length>0||"请务必输入密码"},function(e){return e&&e.length>=6&&e.length<=18||"密码长度在6-18字符之间"},function(n){return n&&e.test(n)||"密码必须包含大写字母、小写字母、数字"}],pwdt:null,isPwd:!0,checkCode:null,show:!0,count:"",timer:null}},mounted:function(){console.log(this.$q.version)},methods:{sendCode:function(){var e=this,n=60;this.timer||(this.count=n,this.show=!1,this.timer=setInterval((function(){e.count>0&&e.count<=n?e.count--:(e.show=!0,clearInterval(e.timer),e.timer=null)}),1e3)),this.sentCode=!0,this.$axios.post("/code/"+this.phone).then((function(n){200==n.type&&(e.checkCode=n.data,console.log("checkCode:"+e.checkCode))})).catch((function(e){console.log(e)}))},onSubmit:function(){var e=this;this.$axios.post("/register",{username:this.name,password:this.password,mobile:this.phone,code:this.code}).then((function(n){200==n.type&&(e.$q.notify({color:"positive",textColor:"white",icon:"cloud_done",message:"注册成功！"}),e.toLogin())})).catch((function(e){console.log(e)}))},onReset:function(){this.name=null,this.phone=null,this.password=null,this.pwdt=null,this.code=!1},toLogin:function(){this.$router.push({name:"Login"})}}}),l=i,u=t("c701"),a=t("0378"),c=t("27f9"),r=t("9c40"),d=t("0016"),h=t("3117"),p=t.n(h),f=Object(u["a"])(l,o,s,!1,null,"3ba6f8d2",null);n["default"]=f.exports;p()(f,"components",{QForm:a["a"],QInput:c["a"],QBtn:r["a"],QIcon:d["a"]})}}]);