var Status = Backbone.Model.extend({
	urlRoot : "http://sharemylocation-shekhargulati.rhcloud.com/api/v1/statuses"
});
var Statuses = Backbone.Collection.extend({
	url : "http://sharemylocation-shekhargulati.rhcloud.com/api/v1/statuses"
});

var PostView = Backbone.View.extend({
	el : ".page",
	
	events : {
		"submit #postForm" : "postStatus"
	},
	
	postStatus : function(event){
		event.preventDefault();
		console.log("In postStatus()... ");
		var status = $("textarea#status").val();
		var postedBy = $("#postedBy").val();
		var useCurrentLocation = $('#useCurrentLocation').is(":checked") ? true : false;
		alert(status + postedBy + useCurrentLocation);
		
		if(useCurrentLocation){
			getCurrentPosition(callback , status , postedBy);
		}else{
			var obj = {
				status : status,
				postedBy : postedBy,
			};
			var model = new Status();
			model.save(obj , {
				success : function(){
					console.log("Post successfully saved without location..");
					app.navigate("#",{trigger:true});
				}
			});
		}
	},
	
	render : function(){
		var template = _.template($("#status-post-template").html() , {});
		this.$el.html(template);
	}
});

function getCurrentPosition(callback , status , postedBy){

	navigator.geolocation.getCurrentPosition(function(position){

					var longitude = position.coords.longitude;
			    	var latitude = position.coords.latitude;
			    	callback(latitude , longitude , status , postedBy);
				}, function(e){
					switch (e.code) {
						case e.PERMISSION_DENIED:
							alert('You have denied access to your position. You will ' +
									'not get the most out of the application now.'); 
							break;
						case e.POSITION_UNAVAILABLE:
							alert('There was a problem getting your position.'); 
							break;
						case e.TIMEOUT:
									alert('The application has timed out attempting to get ' +
											'your location.'); 
							break;
						default:
							alert('There was a horrible Geolocation error that has ' +
									'not been defined.');
					}
				},
					{ timeout: 45000 }

				);
}

function callback(latitude , longitude , status , postedBy){
	var obj = {
			status : status,
			postedBy : postedBy,
			location : {
				type : "POINT",
				coordinates : [longitude , latitude]
			}
		};
	var model = new Status();
	model.save(obj, {
		success : function(){
			console.log("Post successfully saved..");
			app.navigate("#",{trigger:true});
		}
	});
}

var HomeView = Backbone.View.extend({
	el : ".page",
	render : function() {
		var statuses = new Statuses();
		var that = this;
		statuses.fetch({
			success : function(statuses) {
				var template = _.template($("#status-list-template").html(), {
					statuses : statuses.models
				});
				that.$el.html(template);
			}
		});

	}
});

var Router = Backbone.Router.extend({
	routes : {
		"" : "home",
		"post" : "post",
		"search" : "search"
	},

	home : function() {
		console.log("Show home page");
		var homeView = new HomeView();
		homeView.render();
	},

	post : function() {
		console.log("Show post page");
		var postView = new PostView();
		postView.render();
	},

	search : function() {
		console.log("Show search page");
	}
});

var app = new Router();
Backbone.history.start();