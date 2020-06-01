var app = new Vue({
	el: "#page",
	data: {
		content: {
			username: "",
			password: "",
			rememberMe: ""
		},
	},
	methods: {
		gettoken: gettoken,
	}
});

function gettoken() {
	console.log("获取Token------");
	var data = this.content;
	fetch("http://localhost:8102/auth/login", {
		method: 'POST',
		body: JSON.stringify(data),
		headers: {
			'content-type': 'application/json'
		}
	}).then(response => { /* do something */
		response.headers.forEach((v, k) => {
			//console.log(k, v);
			//现在是判断 response中是否有token字段
				//if (localStorage.getItem(data.username)!=null) {//说明本地存在此用户的token
					//console.log("本地存储中没有对应username的token，第一次登录==="+k.toString());
					if (k.toString() == "token") {
						console.log("成功获取token====" + k);
						//如果用户名不唯一，可以定义一个唯一字段，从本次请求返回获取
						localStorage.setItem(data.username, v);
						//window.location.href="http://blog.csdn.net/weixin_44811035";
						console.log("打印本地中的token----" + localStorage.getItem(data.username));
						//获取到了token后自动跳转tasks页面，并且带上token
						jump();
					}
				//} else { 
					//console.log("本地存储中有对应username的token，对比是否过期");
					//return;
					//token过期
				//}
		});
	});
}



function jump() {
	var a = app.content;
	if (localStorage.getItem(a.username) != null) {
		fetch("http://localhost:8102/tasks/listTasks", {
			method: 'GET',
			//body: JSON.stringify(a),
			headers: {
				'content-type': 'application/json',
				'Authorization': localStorage.getItem(a.username),
			}
		}).then(response => {
			console.log("跳转listTasks 页面");
		});
	}
}
//  /auth/login路径的话进去不会验证你是否带了Token，而是直接验证账号密码，成功后返回给你一个Token
//  其他路径登录时 会验证token 是否一致
