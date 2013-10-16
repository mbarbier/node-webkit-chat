var mb = mb || {};
mb.ui = mb.ui || {};

function bindNW(id, object) {
	return new mb.ui.NWProxy(id, object);
}


mb.ui.NWProxy = function(id, element) {
	this.id = id;
	var that = this;
	if(typeof process!='undefined') {
		that.gui = require('nw.gui');
		that.win = that.gui.Window.get();
		var nwversion = process.versions['node-webkit'];
		sendDataToServer(that.id, {
			"info" : { 
				"nwversion" : nwversion
			}
		});
	} else {
		sendDataToServer(that.id, {
			"nwversion" : "KO"
		});
	}
};

// update method (server to native)
mb.ui.NWProxy.prototype.update = function(data) {
	var that = this;
	if (data.win) {
		if (data.win.show) {
			that.win.show();
		} else if(data.win.hide) {
			that.win.hide();
		} else if(data.win.focus) {
			that.win.focus();
		} else if(data.win.restore) {
			that.win.restore();
		} else if(data.win.addHandler) {
			var type = data.win.addHandler;
			that.win.on(type, function() {
				sendDataToServer(that.id, {
					"win" : { 
						"on" : type
					}
				});
			});
		}
	}
};
