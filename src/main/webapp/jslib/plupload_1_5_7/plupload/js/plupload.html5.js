(function(k, m, l, g) {
	var d = {}, j;
	function c(s) {
		var r = s.naturalWidth, u = s.naturalHeight;
		if (r * u > 1024 * 1024) {
			var t = m.createElement("canvas");
			t.width = t.height = 1;
			var q = t.getContext("2d");
			q.drawImage(s, -r + 1, 0);
			return q.getImageData(0, 0, 1, 1).data[3] === 0
		} else {
			return false
		}
	}
	function f(u, r, z) {
		var q = m.createElement("canvas");
		q.width = 1;
		q.height = z;
		var A = q.getContext("2d");
		A.drawImage(u, 0, 0);
		var t = A.getImageData(0, 0, 1, z).data;
		var x = 0;
		var v = z;
		var y = z;
		while (y > x) {
			var s = t[(y - 1) * 4 + 3];
			if (s === 0) {
				v = y
			} else {
				x = y
			}
			y = (v + x) >> 1
		}
		var w = (y / z);
		return (w === 0) ? 1 : w
	}
	function o(K, s, t) {
		var v = K.naturalWidth, z = K.naturalHeight;
		var E = t.width, B = t.height;
		var F = s.getContext("2d");
		F.save();
		var r = c(K);
		if (r) {
			v /= 2;
			z /= 2
		}
		var I = 1024;
		var q = m.createElement("canvas");
		q.width = q.height = I;
		var u = q.getContext("2d");
		var G = f(K, v, z);
		var A = 0;
		while (A < z) {
			var J = A + I > z ? z - A : I;
			var C = 0;
			while (C < v) {
				var D = C + I > v ? v - C : I;
				u.clearRect(0, 0, I, I);
				u.drawImage(K, -C, -A);
				var x = (C * E / v) << 0;
				var y = Math.ceil(D * E / v);
				var w = (A * B / z / G) << 0;
				var H = Math.ceil(J * B / z / G);
				F.drawImage(q, 0, 0, D, J, x, w, y, H);
				C += I
			}
			A += I
		}
		F.restore();
		q = u = null
	}
	function p(r, s) {
		var q;
		if ("FileReader" in k) {
			q = new FileReader();
			q.readAsDataURL(r);
			q.onload = function() {
				s(q.result)
			}
		} else {
			return s(r.getAsDataURL())
		}
	}
	function n(r, s) {
		var q;
		if ("FileReader" in k) {
			q = new FileReader();
			q.readAsBinaryString(r);
			q.onload = function() {
				s(q.result)
			}
		} else {
			return s(r.getAsBinary())
		}
	}
	function e(u, s, q, y) {
		var t, r, x, v, w = this;
		p(d[u.id], function(z) {
			t = m.createElement("canvas");
			t.style.display = "none";
			m.body.appendChild(t);
			x = new Image();
			x.onerror = x.onabort = function() {
				y({
					success : false
				})
			};
			x.onload = function() {
				var F, A, C, B, E;
				if (!s.width) {
					s.width = x.width
				}
				if (!s.height) {
					s.height = x.height
				}
				v = Math.min(s.width / x.width, s.height / x.height);
				if (v < 1) {
					F = Math.round(x.width * v);
					A = Math.round(x.height * v)
				} else {
					if (s.quality && q === "image/jpeg") {
						F = x.width;
						A = x.height
					} else {
						y({
							success : false
						});
						return
					}
				}
				t.width = F;
				t.height = A;
				o(x, t, {
					width : F,
					height : A
				});
				if (q === "image/jpeg") {
					B = new h(atob(z.substring(z.indexOf("base64,") + 7)));
					if (B.headers && B.headers.length) {
						E = new a();
						if (E.init(B.get("exif")[0])) {
							E.setExif("PixelXDimension", F);
							E.setExif("PixelYDimension", A);
							B.set("exif", E.getBinary());
							if (w.hasEventListener("ExifData")) {
								w.trigger("ExifData", u, E.EXIF())
							}
							if (w.hasEventListener("GpsData")) {
								w.trigger("GpsData", u, E.GPS())
							}
						}
					}
				}
				if (s.quality && q === "image/jpeg") {
					try {
						z = t.toDataURL(q, s.quality / 100)
					} catch (D) {
						z = t.toDataURL(q)
					}
				} else {
					z = t.toDataURL(q)
				}
				z = z.substring(z.indexOf("base64,") + 7);
				z = atob(z);
				if (B && B.headers && B.headers.length) {
					z = B.restore(z);
					B.purge()
				}
				t.parentNode.removeChild(t);
				y({
					success : true,
					data : z
				})
			};
			x.src = z
		})
	}
	l.runtimes.Html5 = l.addRuntime("html5", {
		getFeatures : function() {
			var v, r, u, t, s, q;
			r = u = s = q = false;
			if (k.XMLHttpRequest) {
				v = new XMLHttpRequest();
				u = !!v.upload;
				r = !!(v.sendAsBinary || v.upload)
			}
			if (r) {
				t = !!(v.sendAsBinary || (k.Uint8Array && k.ArrayBuffer));
				s = !!(File && (File.prototype.getAsDataURL || k.FileReader) && t);
				q = !!(File && (File.prototype.mozSlice || File.prototype.webkitSlice || File.prototype.slice))
			}
			j = l.ua.safari && l.ua.windows;
			return {
				html5 : r,
				dragdrop : (function() {
					var w = m.createElement("div");
					return ("draggable" in w) || ("ondragstart" in w && "ondrop" in w)
				}()),
				jpgresize : s,
				pngresize : s,
				multipart : s || !!k.FileReader || !!k.FormData,
				canSendBinary : t,
				cantSendBlobInFormData : !!(l.ua.gecko && k.FormData && k.FileReader && !FileReader.prototype.readAsArrayBuffer) || l.ua.android,
				progress : u,
				chunks : q,
				multi_selection : !(l.ua.safari && l.ua.windows),
				triggerDialog : (l.ua.gecko && k.FormData || l.ua.webkit)
			}
		},
		init : function(s, u) {
			var q, t;
			function r(z) {
				var x, w, y = [], A, v = {};
				for (w = 0; w < z.length; w++) {
					x = z[w];
					if (v[x.name] && l.ua.safari && l.ua.windows) {
						continue
					}
					v[x.name] = true;
					A = l.guid();
					d[A] = x;
					y.push(new l.File(A, x.fileName || x.name, x.fileSize || x.size))
				}
				if (y.length) {
					s.trigger("FilesAdded", y)
				}
			}
			q = this.getFeatures();
			if (!q.html5) {
				u({
					success : false
				});
				return
			}
			s.bind("Init", function(A) {
				var J, I, F = [], z, G, w = A.settings.filters, x, E, v = m.body, H;
				J = m.createElement("div");
				J.id = A.id + "_html5_container";
				l.extend(J.style, {
					position : "absolute",
					background : s.settings.shim_bgcolor || "transparent",
					width : "100px",
					height : "100px",
					overflow : "hidden",
					zIndex : 99999,
					opacity : s.settings.shim_bgcolor ? "" : 0
				});
				J.className = "plupload html5";
				if (s.settings.container) {
					v = m.getElementById(s.settings.container);
					if (l.getStyle(v, "position") === "static") {
						v.style.position = "relative"
					}
				}
				v.appendChild(J);
				no_type_restriction: for (z = 0; z < w.length; z++) {
					x = w[z].extensions.split(/,/);
					for (G = 0; G < x.length; G++) {
						if (x[G] === "*") {
							F = [];
							break no_type_restriction
						}
						E = l.mimeTypes[x[G]];
						if (E && l.inArray(E, F) === -1) {
							F.push(E)
						}
					}
				}
				J.innerHTML = '<input id="' + s.id + '_html5"  style="font-size:999px" type="file" accept="' + F.join(",") + '" ' + (s.settings.multi_selection && s.features.multi_selection ? 'multiple="multiple"' : "") + " />";
				J.scrollTop = 100;
				H = m.getElementById(s.id + "_html5");
				if (A.features.triggerDialog) {
					l.extend(H.style, {
						position : "absolute",
						width : "100%",
						height : "100%"
					})
				} else {
					l.extend(H.style, {
						cssFloat : "right",
						styleFloat : "right"
					})
				}
				H.onchange = function() {
					r(this.files);
					this.value = ""
				};
				I = m.getElementById(A.settings.browse_button);
				if (I) {
					var C = A.settings.browse_button_hover, D = A.settings.browse_button_active, B = A.features.triggerDialog ? I : J;
					if (C) {
						l.addEvent(B, "mouseover", function() {
							l.addClass(I, C)
						}, A.id);
						l.addEvent(B, "mouseout", function() {
							l.removeClass(I, C)
						}, A.id)
					}
					if (D) {
						l.addEvent(B, "mousedown", function() {
							l.addClass(I, D)
						}, A.id);
						l.addEvent(m.body, "mouseup", function() {
							l.removeClass(I, D)
						}, A.id)
					}
					if (A.features.triggerDialog) {
						l.addEvent(I, "click", function(K) {
							var y = m.getElementById(A.id + "_html5");
							if (y && !y.disabled) {
								y.click()
							}
							K.preventDefault()
						}, A.id)
					}
				}
			});
			s.bind("PostInit", function() {
				var v = m.getElementById(s.settings.drop_element);
				if (v) {
					if (j) {
						l.addEvent(v, "dragenter", function(z) {
							var y, w, x;
							y = m.getElementById(s.id + "_drop");
							if (!y) {
								y = m.createElement("input");
								y.setAttribute("type", "file");
								y.setAttribute("id", s.id + "_drop");
								y.setAttribute("multiple", "multiple");
								l.addEvent(y, "change", function() {
									r(this.files);
									l.removeEvent(y, "change", s.id);
									y.parentNode.removeChild(y)
								}, s.id);
								l.addEvent(y, "dragover", function(A) {
									A.stopPropagation()
								}, s.id);
								v.appendChild(y)
							}
							w = l.getPos(v, m.getElementById(s.settings.container));
							x = l.getSize(v);
							if (l.getStyle(v, "position") === "static") {
								l.extend(v.style, {
									position : "relative"
								})
							}
							l.extend(y.style, {
								position : "absolute",
								display : "block",
								top : 0,
								left : 0,
								width : x.w + "px",
								height : x.h + "px",
								opacity : 0
							})
						}, s.id);
						return
					}
					l.addEvent(v, "dragover", function(w) {
						w.preventDefault()
					}, s.id);
					l.addEvent(v, "drop", function(x) {
						var w = x.dataTransfer;
						if (w && w.files) {
							r(w.files)
						}
						x.preventDefault()
					}, s.id)
				}
			});
			s.bind("Refresh", function(v) {
				var w, x, y, A, z;
				w = m.getElementById(s.settings.browse_button);
				if (w) {
					x = l.getPos(w, m.getElementById(v.settings.container));
					y = l.getSize(w);
					A = m.getElementById(s.id + "_html5_container");
					l.extend(A.style, {
						top : x.y + "px",
						left : x.x + "px",
						width : y.w + "px",
						height : y.h + "px"
					});
					if (s.features.triggerDialog) {
						if (l.getStyle(w, "position") === "static") {
							l.extend(w.style, {
								position : "relative"
							})
						}
						z = parseInt(l.getStyle(w, "zIndex"), 10);
						if (isNaN(z)) {
							z = 0
						}
						l.extend(w.style, {
							zIndex : z
						});
						l.extend(A.style, {
							zIndex : z - 1
						})
					}
				}
			});
			s.bind("DisableBrowse", function(v, x) {
				var w = m.getElementById(v.id + "_html5");
				if (w) {
					w.disabled = x
				}
			});
			s.bind("CancelUpload", function() {
				if (t && t.abort) {
					t.abort()
				}
			});
			s.bind("UploadFile", function(v, x) {
				var y = v.settings, B, w;
				function A(D, G, C) {
					var E;
					if (File.prototype.slice) {
						try {
							D.slice();
							return D.slice(G, C)
						} catch (F) {
							return D.slice(G, C - G)
						}
					} else {
						if (E = File.prototype.webkitSlice || File.prototype.mozSlice) {
							return E.call(D, G, C)
						} else {
							return null
						}
					}
				}
				function z(C) {
					var F = 0, E = 0;
					function D() {
						var L, P, N, O, K, M, H, G = v.settings.url;
						function J(S) {
							if (t.sendAsBinary) {
								t.sendAsBinary(S)
							} else {
								if (v.features.canSendBinary) {
									var Q = new Uint8Array(S.length);
									for ( var R = 0; R < S.length; R++) {
										Q[R] = (S.charCodeAt(R) & 255)
									}
									t.send(Q.buffer)
								}
							}
						}
						function I(R) {
							var V = 0, W = "----pluploadboundary" + l.guid(), T, S = "--", U = "\r\n", Q = "";
							t = new XMLHttpRequest;
							if (t.upload) {
								t.upload.onprogress = function(X) {
									x.loaded = Math.min(x.size, E + X.loaded - V);
									v.trigger("UploadProgress", x)
								}
							}
							t.onreadystatechange = function() {
								var X, Z;
								if (t.readyState == 4 && v.state !== l.STOPPED) {
									try {
										X = t.status
									} catch (Y) {
										X = 0
									}
									if (X >= 400) {
										v.trigger("Error", {
											code : l.HTTP_ERROR,
											message : l.translate("HTTP Error."),
											file : x,
											status : X
										})
									} else {
										if (N) {
											Z = {
												chunk : F,
												chunks : N,
												response : t.responseText,
												status : X
											};
											v.trigger("ChunkUploaded", x, Z);
											E += M;
											if (Z.cancelled) {
												x.status = l.FAILED;
												return
											}
											x.loaded = Math.min(x.size, (F + 1) * K)
										} else {
											x.loaded = x.size
										}
										v.trigger("UploadProgress", x);
										R = L = T = Q = null;
										if (!N || ++F >= N) {
											x.status = l.DONE;
											v.trigger("FileUploaded", x, {
												response : t.responseText,
												status : X
											})
										} else {
											D()
										}
									}
								}
							};
							if (v.settings.multipart && q.multipart) {
								O.name = x.target_name || x.name;
								t.open("post", G, true);
								l.each(v.settings.headers, function(Y, X) {
									t.setRequestHeader(X, Y)
								});
								if (typeof (R) !== "string" && !!k.FormData) {
									T = new FormData();
									l.each(l.extend(O, v.settings.multipart_params), function(Y, X) {
										T.append(X, Y)
									});
									T.append(v.settings.file_data_name, R);
									t.send(T);
									return
								}
								if (typeof (R) === "string") {
									t.setRequestHeader("Content-Type", "multipart/form-data; boundary=" + W);
									l.each(l.extend(O, v.settings.multipart_params), function(Y, X) {
										Q += S + W + U + 'Content-Disposition: form-data; name="' + X + '"' + U + U;
										Q += unescape(encodeURIComponent(Y)) + U
									});
									H = l.mimeTypes[x.name.replace(/^.+\.([^.]+)/, "$1").toLowerCase()] || "application/octet-stream";
									Q += S + W + U + 'Content-Disposition: form-data; name="' + v.settings.file_data_name + '"; filename="' + unescape(encodeURIComponent(x.name)) + '"' + U + "Content-Type: " + H + U + U + R + U + S + W + S + U;
									V = Q.length - R.length;
									R = Q;
									J(R);
									return
								}
							}
							G = l.buildUrl(v.settings.url, l.extend(O, v.settings.multipart_params));
							t.open("post", G, true);
							t.setRequestHeader("Content-Type", "application/octet-stream");
							l.each(v.settings.headers, function(Y, X) {
								t.setRequestHeader(X, Y)
							});
							if (typeof (R) === "string") {
								J(R)
							} else {
								t.send(R)
							}
						}
						if (x.status == l.DONE || x.status == l.FAILED || v.state == l.STOPPED) {
							return
						}
						O = {
							name : x.target_name || x.name
						};
						if (y.chunk_size && x.size > y.chunk_size && (q.chunks || typeof (C) == "string")) {
							K = y.chunk_size;
							N = Math.ceil(x.size / K);
							M = Math.min(K, x.size - (F * K));
							if (typeof (C) == "string") {
								L = C.substring(F * K, F * K + M)
							} else {
								L = A(C, F * K, F * K + M)
							}
							O.chunk = F;
							O.chunks = N
						} else {
							M = x.size;
							L = C
						}
						if (v.settings.multipart && q.multipart && typeof (L) !== "string" && k.FileReader && q.cantSendBlobInFormData && q.chunks && v.settings.chunk_size) {
							(function() {
								var Q = new FileReader();
								Q.onload = function() {
									I(Q.result);
									Q = null
								};
								Q.readAsBinaryString(L)
							}())
						} else {
							I(L)
						}
					}
					D()
				}
				B = d[x.id];
				if (q.jpgresize && v.settings.resize && /\.(png|jpg|jpeg)$/i.test(x.name)) {
					e.call(v, x, v.settings.resize, /\.png$/i.test(x.name) ? "image/png" : "image/jpeg", function(C) {
						if (C.success) {
							x.size = C.data.length;
							z(C.data)
						} else {
							if (q.chunks) {
								z(B)
							} else {
								n(B, z)
							}
						}
					})
				} else {
					if (!q.chunks && q.jpgresize) {
						n(B, z)
					} else {
						z(B)
					}
				}
			});
			s.bind("Destroy", function(v) {
				var x, y, w = m.body, z = {
					inputContainer : v.id + "_html5_container",
					inputFile : v.id + "_html5",
					browseButton : v.settings.browse_button,
					dropElm : v.settings.drop_element
				};
				for (x in z) {
					y = m.getElementById(z[x]);
					if (y) {
						l.removeAllEvents(y, v.id)
					}
				}
				l.removeAllEvents(m.body, v.id);
				if (v.settings.container) {
					w = m.getElementById(v.settings.container)
				}
				w.removeChild(m.getElementById(z.inputContainer))
			});
			u({
				success : true
			})
		}
	});
	function b() {
		var t = false, r;
		function u(w, y) {
			var v = t ? 0 : -8 * (y - 1), z = 0, x;
			for (x = 0; x < y; x++) {
				z |= (r.charCodeAt(w + x) << Math.abs(v + x * 8))
			}
			return z
		}
		function q(x, v, w) {
			var w = arguments.length === 3 ? w : r.length - v - 1;
			r = r.substr(0, v) + x + r.substr(w + v)
		}
		function s(w, x, z) {
			var A = "", v = t ? 0 : -8 * (z - 1), y;
			for (y = 0; y < z; y++) {
				A += String.fromCharCode((x >> Math.abs(v + y * 8)) & 255)
			}
			q(A, w, z)
		}
		return {
			II : function(v) {
				if (v === g) {
					return t
				} else {
					t = v
				}
			},
			init : function(v) {
				t = false;
				r = v
			},
			SEGMENT : function(v, x, w) {
				switch (arguments.length) {
				case 1:
					return r.substr(v, r.length - v - 1);
				case 2:
					return r.substr(v, x);
				case 3:
					q(w, v, x);
					break;
				default:
					return r
				}
			},
			BYTE : function(v) {
				return u(v, 1)
			},
			SHORT : function(v) {
				return u(v, 2)
			},
			LONG : function(v, w) {
				if (w === g) {
					return u(v, 4)
				} else {
					s(v, w, 4)
				}
			},
			SLONG : function(v) {
				var w = u(v, 4);
				return (w > 2147483647 ? w - 4294967296 : w)
			},
			STRING : function(v, w) {
				var x = "";
				for (w += v; v < w; v++) {
					x += String.fromCharCode(u(v, 1))
				}
				return x
			}
		}
	}
	function h(v) {
		var x = {
			65505 : {
				app : "EXIF",
				name : "APP1",
				signature : "Exif\0"
			},
			65506 : {
				app : "ICC",
				name : "APP2",
				signature : "ICC_PROFILE\0"
			},
			65517 : {
				app : "IPTC",
				name : "APP13",
				signature : "Photoshop 3.0\0"
			}
		}, w = [], u, q, s = g, t = 0, r;
		u = new b();
		u.init(v);
		if (u.SHORT(0) !== 65496) {
			return
		}
		q = 2;
		r = Math.min(1048576, v.length);
		while (q <= r) {
			s = u.SHORT(q);
			if (s >= 65488 && s <= 65495) {
				q += 2;
				continue
			}
			if (s === 65498 || s === 65497) {
				break
			}
			t = u.SHORT(q + 2) + 2;
			if (x[s] && u.STRING(q + 4, x[s].signature.length) === x[s].signature) {
				w.push({
					hex : s,
					app : x[s].app.toUpperCase(),
					name : x[s].name.toUpperCase(),
					start : q,
					length : t,
					segment : u.SEGMENT(q, t)
				})
			}
			q += t
		}
		u.init(null);
		return {
			headers : w,
			restore : function(B) {
				u.init(B);
				var z = new h(B);
				if (!z.headers) {
					return false
				}
				for ( var A = z.headers.length; A > 0; A--) {
					var C = z.headers[A - 1];
					u.SEGMENT(C.start, C.length, "")
				}
				z.purge();
				q = u.SHORT(2) == 65504 ? 4 + u.SHORT(4) : 2;
				for ( var A = 0, y = w.length; A < y; A++) {
					u.SEGMENT(q, 0, w[A].segment);
					q += w[A].length
				}
				return u.SEGMENT()
			},
			get : function(A) {
				var B = [];
				for ( var z = 0, y = w.length; z < y; z++) {
					if (w[z].app === A.toUpperCase()) {
						B.push(w[z].segment)
					}
				}
				return B
			},
			set : function(B, A) {
				var C = [];
				if (typeof (A) === "string") {
					C.push(A)
				} else {
					C = A
				}
				for ( var z = ii = 0, y = w.length; z < y; z++) {
					if (w[z].app === B.toUpperCase()) {
						w[z].segment = C[ii];
						w[z].length = C[ii].length;
						ii++
					}
					if (ii >= C.length) {
						break
					}
				}
			},
			purge : function() {
				w = [];
				u.init(null)
			}
		}
	}
	function a() {
		var t, q, r = {}, w;
		t = new b();
		q = {
			tiff : {
				274 : "Orientation",
				34665 : "ExifIFDPointer",
				34853 : "GPSInfoIFDPointer"
			},
			exif : {
				36864 : "ExifVersion",
				40961 : "ColorSpace",
				40962 : "PixelXDimension",
				40963 : "PixelYDimension",
				36867 : "DateTimeOriginal",
				33434 : "ExposureTime",
				33437 : "FNumber",
				34855 : "ISOSpeedRatings",
				37377 : "ShutterSpeedValue",
				37378 : "ApertureValue",
				37383 : "MeteringMode",
				37384 : "LightSource",
				37385 : "Flash",
				41986 : "ExposureMode",
				41987 : "WhiteBalance",
				41990 : "SceneCaptureType",
				41988 : "DigitalZoomRatio",
				41992 : "Contrast",
				41993 : "Saturation",
				41994 : "Sharpness"
			},
			gps : {
				0 : "GPSVersionID",
				1 : "GPSLatitudeRef",
				2 : "GPSLatitude",
				3 : "GPSLongitudeRef",
				4 : "GPSLongitude"
			}
		};
		w = {
			ColorSpace : {
				1 : "sRGB",
				0 : "Uncalibrated"
			},
			MeteringMode : {
				0 : "Unknown",
				1 : "Average",
				2 : "CenterWeightedAverage",
				3 : "Spot",
				4 : "MultiSpot",
				5 : "Pattern",
				6 : "Partial",
				255 : "Other"
			},
			LightSource : {
				1 : "Daylight",
				2 : "Fliorescent",
				3 : "Tungsten",
				4 : "Flash",
				9 : "Fine weather",
				10 : "Cloudy weather",
				11 : "Shade",
				12 : "Daylight fluorescent (D 5700 - 7100K)",
				13 : "Day white fluorescent (N 4600 -5400K)",
				14 : "Cool white fluorescent (W 3900 - 4500K)",
				15 : "White fluorescent (WW 3200 - 3700K)",
				17 : "Standard light A",
				18 : "Standard light B",
				19 : "Standard light C",
				20 : "D55",
				21 : "D65",
				22 : "D75",
				23 : "D50",
				24 : "ISO studio tungsten",
				255 : "Other"
			},
			Flash : {
				0 : "Flash did not fire.",
				1 : "Flash fired.",
				5 : "Strobe return light not detected.",
				7 : "Strobe return light detected.",
				9 : "Flash fired, compulsory flash mode",
				13 : "Flash fired, compulsory flash mode, return light not detected",
				15 : "Flash fired, compulsory flash mode, return light detected",
				16 : "Flash did not fire, compulsory flash mode",
				24 : "Flash did not fire, auto mode",
				25 : "Flash fired, auto mode",
				29 : "Flash fired, auto mode, return light not detected",
				31 : "Flash fired, auto mode, return light detected",
				32 : "No flash function",
				65 : "Flash fired, red-eye reduction mode",
				69 : "Flash fired, red-eye reduction mode, return light not detected",
				71 : "Flash fired, red-eye reduction mode, return light detected",
				73 : "Flash fired, compulsory flash mode, red-eye reduction mode",
				77 : "Flash fired, compulsory flash mode, red-eye reduction mode, return light not detected",
				79 : "Flash fired, compulsory flash mode, red-eye reduction mode, return light detected",
				89 : "Flash fired, auto mode, red-eye reduction mode",
				93 : "Flash fired, auto mode, return light not detected, red-eye reduction mode",
				95 : "Flash fired, auto mode, return light detected, red-eye reduction mode"
			},
			ExposureMode : {
				0 : "Auto exposure",
				1 : "Manual exposure",
				2 : "Auto bracket"
			},
			WhiteBalance : {
				0 : "Auto white balance",
				1 : "Manual white balance"
			},
			SceneCaptureType : {
				0 : "Standard",
				1 : "Landscape",
				2 : "Portrait",
				3 : "Night scene"
			},
			Contrast : {
				0 : "Normal",
				1 : "Soft",
				2 : "Hard"
			},
			Saturation : {
				0 : "Normal",
				1 : "Low saturation",
				2 : "High saturation"
			},
			Sharpness : {
				0 : "Normal",
				1 : "Soft",
				2 : "Hard"
			},
			GPSLatitudeRef : {
				N : "North latitude",
				S : "South latitude"
			},
			GPSLongitudeRef : {
				E : "East longitude",
				W : "West longitude"
			}
		};
		function s(x, F) {
			var z = t.SHORT(x), C, I, J, E, D, y, A, G, H = [], B = {};
			for (C = 0; C < z; C++) {
				A = y = x + 12 * C + 2;
				J = F[t.SHORT(A)];
				if (J === g) {
					continue
				}
				E = t.SHORT(A += 2);
				D = t.LONG(A += 2);
				A += 4;
				H = [];
				switch (E) {
				case 1:
				case 7:
					if (D > 4) {
						A = t.LONG(A) + r.tiffHeader
					}
					for (I = 0; I < D; I++) {
						H[I] = t.BYTE(A + I)
					}
					break;
				case 2:
					if (D > 4) {
						A = t.LONG(A) + r.tiffHeader
					}
					B[J] = t.STRING(A, D - 1);
					continue;
				case 3:
					if (D > 2) {
						A = t.LONG(A) + r.tiffHeader
					}
					for (I = 0; I < D; I++) {
						H[I] = t.SHORT(A + I * 2)
					}
					break;
				case 4:
					if (D > 1) {
						A = t.LONG(A) + r.tiffHeader
					}
					for (I = 0; I < D; I++) {
						H[I] = t.LONG(A + I * 4)
					}
					break;
				case 5:
					A = t.LONG(A) + r.tiffHeader;
					for (I = 0; I < D; I++) {
						H[I] = t.LONG(A + I * 4) / t.LONG(A + I * 4 + 4)
					}
					break;
				case 9:
					A = t.LONG(A) + r.tiffHeader;
					for (I = 0; I < D; I++) {
						H[I] = t.SLONG(A + I * 4)
					}
					break;
				case 10:
					A = t.LONG(A) + r.tiffHeader;
					for (I = 0; I < D; I++) {
						H[I] = t.SLONG(A + I * 4) / t.SLONG(A + I * 4 + 4)
					}
					break;
				default:
					continue
				}
				G = (D == 1 ? H[0] : H);
				if (w.hasOwnProperty(J) && typeof G != "object") {
					B[J] = w[J][G]
				} else {
					B[J] = G
				}
			}
			return B
		}
		function v() {
			var y = g, x = r.tiffHeader;
			t.II(t.SHORT(x) == 18761);
			if (t.SHORT(x += 2) !== 42) {
				return false
			}
			r.IFD0 = r.tiffHeader + t.LONG(x += 2);
			y = s(r.IFD0, q.tiff);
			r.exifIFD = ("ExifIFDPointer" in y ? r.tiffHeader + y.ExifIFDPointer : g);
			r.gpsIFD = ("GPSInfoIFDPointer" in y ? r.tiffHeader + y.GPSInfoIFDPointer : g);
			return true
		}
		function u(z, x, C) {
			var E, B, A, D = 0;
			if (typeof (x) === "string") {
				var y = q[z.toLowerCase()];
				for (hex in y) {
					if (y[hex] === x) {
						x = hex;
						break
					}
				}
			}
			E = r[z.toLowerCase() + "IFD"];
			B = t.SHORT(E);
			for (i = 0; i < B; i++) {
				A = E + 12 * i + 2;
				if (t.SHORT(A) == x) {
					D = A + 8;
					break
				}
			}
			if (!D) {
				return false
			}
			t.LONG(D, C);
			return true
		}
		return {
			init : function(x) {
				r = {
					tiffHeader : 10
				};
				if (x === g || !x.length) {
					return false
				}
				t.init(x);
				if (t.SHORT(0) === 65505 && t.STRING(4, 5).toUpperCase() === "EXIF\0") {
					return v()
				}
				return false
			},
			EXIF : function() {
				var y;
				y = s(r.exifIFD, q.exif);
				if (y.ExifVersion && l.typeOf(y.ExifVersion) === "array") {
					for ( var z = 0, x = ""; z < y.ExifVersion.length; z++) {
						x += String.fromCharCode(y.ExifVersion[z])
					}
					y.ExifVersion = x
				}
				return y
			},
			GPS : function() {
				var x;
				x = s(r.gpsIFD, q.gps);
				if (x.GPSVersionID) {
					x.GPSVersionID = x.GPSVersionID.join(".")
				}
				return x
			},
			setExif : function(x, y) {
				if (x !== "PixelXDimension" && x !== "PixelYDimension") {
					return false
				}
				return u("exif", x, y)
			},
			getBinary : function() {
				return t.SEGMENT()
			}
		}
	}
})(window, document, plupload);