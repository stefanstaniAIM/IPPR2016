(function () {
    'use strict';

    angular.module('uiJoint.jointShape', [])
        .factory('jointShape', jointShape);

    /** @ngInject */
    function jointShape($log, jointWindow, jointConfig) {
        var service = this;

        service.subjectElementDefaults = {};
        service.sendStateElementDefaults = {};
        service.receiveStateElementDefaults = {};
        service.functionStateElementDefaults = {};
        service.messageConnectorElementDefaults = {};

        service.init = function () {
            service.subjectElementDefaults = jointConfig.getSubjectElementDefaults();
            service.sendStateElementDefaults = jointConfig.getSendStateElementDefaults();
            service.receiveStateElementDefaults = jointConfig.getReceiveStateElementDefaults();
            service.functionStateElementDefaults = jointConfig.getFunctionStateElementDefaults();
            service.messageConnectorElementDefaults = jointConfig.getMessageConnectorElementDefaults();
        };

        service.highlightElement = function (element) {
            if (element.model.get('type') === 'sid.SubjectElement') {
                element.model.toFront();
                element.$box.css({border: '1px dashed #000000'});
                element.$box.find('.sb-remove-btn').css({visibility: 'visible'});
                element.$box.find('.sb-connector-btn').css({visibility: 'visible'});
            } else if (element.model.get('type') === 'sid.MessageConnectorElement') {
                element.model.toFront();
                element.$box.css({border: '1px dashed #000000'});
                element.$box.find('.msg-remove-btn').css({visibility: 'visible'});
            } else if (element.model.get('type') === 'sbd.SendStateElement') {
                element.model.toFront();
                element.$box.css({border: '1px dashed #000000'});
                element.$box.find('.ss-remove-btn').css({visibility: 'visible'});
                element.$box.find('.ss-connector-btn').css({visibility: 'visible'});
            } else if (element.model.get('type') === 'sbd.ReceiveStateElement') {
                element.model.toFront();
                element.$box.css({border: '1px dashed #000000'});
                element.$box.find('.rs-remove-btn').css({visibility: 'visible'});
                element.$box.find('.rs-connector-btn').css({visibility: 'visible'});
            } else if (element.model.get('type') === 'sbd.FunctionStateElement') {
                element.model.toFront();
                element.$box.css({border: '1px dashed #000000'});
                element.$box.find('.fs-remove-btn').css({visibility: 'visible'});
                element.$box.find('.fs-connector-btn').css({visibility: 'visible'});
            } else if (element.model.get('type') === 'sbd.FromSendStateConnectorElement') {
                element.model.toFront();
                element.$box.css({border: '1px dashed #000000'});
                element.$box.find('.fss-remove-btn').css({visibility: 'visible'});
            } else if (element.model.get('type') === 'sbd.FromReceiveStateConnectorElement') {
                element.model.toFront();
                element.$box.css({border: '1px dashed #000000'});
                element.$box.find('.frs-remove-btn').css({visibility: 'visible'});
            } else if (element.model.get('type') === 'sbd.FromFunctionStateConnectorElement') {
                element.model.toFront();
                element.$box.css({border: '1px dashed #000000'});
                element.$box.find('.ffs-remove-btn').css({visibility: 'visible'});
            }
        };

        service.unhighlightElement = function (element) {
            if (element.model.get('type') === 'sid.SubjectElement') {
                element.$box.css({border: 'none'});
                element.$box.find('.sb-remove-btn').css({visibility: 'hidden'});
                element.$box.find('.sb-connector-btn').css({visibility: 'hidden'});
            } else if (element.model.get('type') === 'sid.MessageConnectorElement') {
                element.$box.css({border: 'none'});
                element.$box.find('.msg-remove-btn').css({visibility: 'hidden'});
            } else if (element.model.get('type') === 'sbd.SendStateElement') {
                element.$box.css({border: 'none'});
                element.$box.find('.ss-remove-btn').css({visibility: 'hidden'});
                element.$box.find('.ss-connector-btn').css({visibility: 'hidden'});
            } else if (element.model.get('type') === 'sbd.ReceiveStateElement') {
                element.$box.css({border: 'none'});
                element.$box.find('.rs-remove-btn').css({visibility: 'hidden'});
                element.$box.find('.rs-connector-btn').css({visibility: 'hidden'});
            } else if (element.model.get('type') === 'sbd.FunctionStateElement') {
                element.$box.css({border: 'none'});
                element.$box.find('.fs-remove-btn').css({visibility: 'hidden'});
                element.$box.find('.fs-connector-btn').css({visibility: 'hidden'});
            } else if (element.model.get('type') === 'sbd.FromSendStateConnectorElement') {
                element.model.toFront();
                element.$box.css({border: 'none'});
                element.$box.find('.fss-remove-btn').css({visibility: 'hidden'});
            } else if (element.model.get('type') === 'sbd.FromReceiveStateConnectorElement') {
                element.model.toFront();
                element.$box.css({border: 'none'});
                element.$box.find('.frs-remove-btn').css({visibility: 'hidden'});
            } else if (element.model.get('type') === 'sbd.FromFunctionStateConnectorElement') {
                element.model.toFront();
                element.$box.css({border: 'none'});
                element.$box.find('.ffs-remove-btn').css({visibility: 'hidden'});
            }
        };

        service.unhighlightAll = function (elements) {
            elements.forEach(function (el) {
                if (el.model.get('type') === 'sid.SubjectElement') {
                    el.$box.css({border: 'none'});
                    el.$box.find('.sb-remove-btn').css({visibility: 'hidden'});
                    el.$box.find('.sb-connector-btn').css({visibility: 'hidden'});
                } else if (el.model.get('type') === 'sid.MessageConnectorElement') {
                    el.$box.css({border: 'none'});
                    el.$box.find('.msg-remove-btn').css({visibility: 'hidden'});
                } else if (el.model.get('type') === 'sbd.SendStateElement') {
                    el.$box.css({border: 'none'});
                    el.$box.find('.ss-remove-btn').css({visibility: 'hidden'});
                    el.$box.find('.ss-connector-btn').css({visibility: 'hidden'});
                } else if (el.model.get('type') === 'sbd.ReceiveStateElement') {
                    el.$box.css({border: 'none'});
                    el.$box.find('.rs-remove-btn').css({visibility: 'hidden'});
                    el.$box.find('.rs-connector-btn').css({visibility: 'hidden'});
                } else if (el.model.get('type') === 'sbd.FunctionStateElement') {
                    el.$box.css({border: 'none'});
                    el.$box.find('.fs-remove-btn').css({visibility: 'hidden'});
                    el.$box.find('.fs-connector-btn').css({visibility: 'hidden'});
                } else if (el.model.get('type') === 'sbd.FromSendStateConnectorElement') {
                    el.model.toFront();
                    el.$box.css({border: 'none'});
                    el.$box.find('.fss-remove-btn').css({visibility: 'hidden'});
                } else if (el.model.get('type') === 'sbd.FromReceiveStateConnectorElement') {
                    el.model.toFront();
                    el.$box.css({border: 'none'});
                    el.$box.find('.frs-remove-btn').css({visibility: 'hidden'});
                } else if (el.model.get('type') === 'sbd.FromFunctionStateConnectorElement') {
                    el.model.toFront();
                    el.$box.css({border: 'none'});
                    el.$box.find('.ffs-remove-btn').css({visibility: 'hidden'});
                }
            });
        };

        /*
         * Custom elements
         */

        //SID group
        jointWindow.shapes.sid = {};

        //
        //SubjectElement
        //
        jointWindow.shapes.sid.SubjectElement = jointWindow.shapes.basic.Rect.extend({
            defaults: jointWindow.util.deepSupplement({
                type: 'sid.SubjectElement'
            }, jointWindow.shapes.basic.Rect.prototype.defaults)
        });

        //Custom view for SubjectElement that displays an HTML div above it
        jointWindow.shapes.sid.SubjectElementView = jointWindow.dia.ElementView.extend({

            template: [
                '<div class="subject-element">',
                '<div class="subject-element-inner">',
                '<div class="sb-name"></div>',
                '<button class="sb-remove-btn"><i class="fa fa-trash-o" aria-hidden="true"></i></button>',
                '<button class="sb-connector-btn"><i class="fa fa-long-arrow-right" aria-hidden="true"></i></button>',
                '</div>',
                '</div>'
            ].join(''),

            initialize: function () {
                _.bindAll(this, 'updateBox');
                jointWindow.dia.ElementView.prototype.initialize.apply(this, arguments);

                this.$box = $(_.template(this.template)());

                // This is an example of reacting on the input change and storing the input data in the cell model.
                this.$box.find('.sb-remove-btn').on('click', _.bind(this.model.remove, this.model));

                // Update the box position whenever the underlying model changes.
                this.model.on('change', this.updateBox, this);
                // Remove the box when the model gets removed from the graph.
                this.model.on('remove', this.removeBox, this);

                this.updateBox();
            },
            render: function () {
                jointWindow.dia.ElementView.prototype.render.apply(this, arguments);
                this.paper.$el.prepend(this.$box);
                this.updateBox();
                return this;
            },
            updateBox: function () {
                // Set the position and dimension of the box so that it covers the JointJS element.
                var bbox = this.model.getBBox();
                var zIndex = this.model.get('z');
                var sbName = this.$box.find('.sb-name');

                // Example of updating the HTML with a data stored in the cell model.
                sbName.text(shorten(this.model.get('customAttrs').name, 90));

                //this.$box.find('span').text(this.model.get('select'));

                this.$box.css({
                    width: bbox.width + 10,
                    height: bbox.height + 10,
                    left: bbox.x - 5,
                    top: bbox.y - 5,
                    'z-index': zIndex
                });

                this.$box.find('.subject-element-inner').css({
                    position: 'fixed',
                    left: bbox.x,
                    top: bbox.y
                });

                sbName.css({
                    position: 'fixed',
                    left: bbox.x + (bbox.width / 2) - (sbName.width() / 2),
                    top: bbox.y + (bbox.height / 2) - (sbName.height() / 2)
                });
            },
            removeBox: function (evt) {
                this.$box.remove();
            }
        });

        //Create new SubjectElement
        service.subjectElement = function (options) {
            options = options || service.subjectElementDefaults;
            return new jointWindow.shapes.sid.SubjectElement(options);
        };

        //
        //MessageConnectorElement
        //
        jointWindow.shapes.sid.MessageConnectorElement = jointWindow.dia.Link.extend({
            defaults: jointWindow.util.deepSupplement({
                type: 'sid.MessageConnectorElement'
            }, jointWindow.dia.Link.prototype.defaults)
        });

        //Custom view for MessageConnectorElement that displays an HTML div above it
        jointWindow.shapes.sid.MessageConnectorElementView = jointWindow.dia.LinkView.extend({

            template: [
                '<div class="message-connector-element">',
                '<div class="message-connector-element-inner">',
                '<div class="msg-unidirectional"><i class="fa fa-long-arrow-right" aria-hidden="true"></i></div>',
                '<div class="msg-bidirectional">',
                '<div class="msg-bidirectional-right"><i class="fa fa-long-arrow-right" aria-hidden="true"></i></div>',
                '<div class="msg-bidirectional-left"><i class="fa fa-long-arrow-left" aria-hidden="true"></i></div>',
                '</div>',
                '<button class="msg-remove-btn"><i class="fa fa-trash-o" aria-hidden="true"></i></button>',
                '</div>',
                '</div>'
            ].join(''),

            initialize: function () {
                _.bindAll(this, 'updateConnector');
                jointWindow.dia.LinkView.prototype.initialize.apply(this, arguments);

                this.$box = $(_.template(this.template)());

                this.$box.on('click', _.bind(this.onClick, this));

                // This is an example of reacting on the input change and storing the input data in the cell model.
                this.$box.find('.msg-remove-btn').on('click', _.bind(this.model.remove, this.model));

                // Update the box position whenever the underlying model changes.
                this.model.on('change', this.updateConnector, this);
                // Remove the box when the model gets removed from the graph.
                this.model.on('remove', this.removeBox, this);

                this.updateConnector();
            },
            onClick: function (evt) {
                var p = this.paper.snapToGrid({
                    x: evt.originalEvent.x,
                    y: evt.originalEvent.y
                });
                this.pointerdown(evt, p.x, p.y);
                this.model.attributes.vertices = [];
                this.update();
                this.updateConnector();
            },
            render: function () {
                jointWindow.dia.LinkView.prototype.render.apply(this, arguments);
                this.paper.$el.prepend(this.$box);
                this.updateConnector();
                return this;
            },
            updateConnector: function () {
                var bbox = this.getBBox();
                var zIndex = this.model.get('z');

                var msgUnidirectional = this.$box.find('.msg-unidirectional');
                var msgBidirectional = this.$box.find('.msg-bidirectional');
                var msgBidirectionalRight = this.$box.find('.msg-bidirectional-right');
                var msgBidirectionalLeft = this.$box.find('.msg-bidirectional-left');

                if (this.model.get('customAttrs').isBidirectional) {
                    msgBidirectional.css({
                        visibility: 'visible'
                    });
                    msgUnidirectional.css({
                        visibility: 'hidden'
                    });
                    this.model.attr({
                        '.marker-source': {fill: '#000000', d: 'M 10 0 L 0 5 L 10 10 z'}
                    });
                } else {
                    msgBidirectional.css({
                        visibility: 'hidden'
                    });
                    msgUnidirectional.css({
                        visibility: 'visible'
                    });
                    this.model.removeAttr('.marker-source');
                }

                var position = {x: 0, y: 0};
                if (typeof this._V !== 'undefined') {
                    var connectionEl = this.$('.connection')[0];
                    var connectionLength = connectionEl.getTotalLength();
                    position = connectionEl.getPointAtLength(connectionLength / 2);
                }

                this.$box.css({
                    width: 90,
                    height: 60,
                    //left: position.x + (bbox.width / 2) - 80,
                    //top: position.y + (bbox.height / 2) - 30,
                    left: position.x - 45,
                    top: position.y - 30,
                    'z-index': zIndex
                });

                this.$box.find('.message-connector-element-inner').css({
                    position: 'fixed',
                    left: position.x - 40,
                    top: position.y - 25
                });

                msgUnidirectional.css({
                    position: 'fixed',
                    left: position.x - (msgUnidirectional.width() / 2),
                    top: position.y - (msgUnidirectional.height() / 2)
                });

                msgBidirectional.css({
                    position: 'fixed',
                    left: position.x - (msgUnidirectional.width() / 2),
                    top: position.y - (msgUnidirectional.height() / 2)
                });

                msgBidirectionalRight.css({
                    position: 'fixed',
                    left: position.x - (msgBidirectionalRight.width() / 2) + 10,
                    top: position.y - (msgBidirectionalRight.height() / 2) - 7
                });

                msgBidirectionalLeft.css({
                    position: 'fixed',
                    left: position.x - (msgBidirectionalLeft.width() / 2) - 10,
                    top: position.y - (msgBidirectionalLeft.height() / 2) + 7
                });
            },
            removeBox: function (evt) {
                this.$box.remove();
            }
        });

        //Create new MessageConnectorElement
        service.messageConnectorElement = function (options) {
            //options = options || service.subjectElementDefaults;
            return new jointWindow.shapes.sid.MessageConnectorElement(options);
        };

        //SBD group
        jointWindow.shapes.sbd = {};

        //
        //SendStateElement
        //
        jointWindow.shapes.sbd.SendStateElement = jointWindow.shapes.basic.Rect.extend({
            defaults: jointWindow.util.deepSupplement({
                type: 'sbd.SendStateElement'
            }, jointWindow.shapes.basic.Rect.prototype.defaults)
        });

        //Custom view for SendStateElement that displays an HTML div above it
        jointWindow.shapes.sbd.SendStateElementView = jointWindow.dia.ElementView.extend({

            template: [
                '<div class="send-state-element">',
                '<div class="send-state-element-inner">',
                '<div class="ss-icon-box">',
                '<div class="ss-icon"></div>',
                '<div class="ss-start-icon"><i class="fa fa-play-circle-o" aria-hidden="true"></i></div>',
                '<div class="ss-end-icon"><i class="fa fa-stop-circle-o" aria-hidden="true"></i></div>',
                '</div>',
                '<span class="ss-name"></span>',
                '<button class="ss-remove-btn"><i class="fa fa-trash-o" aria-hidden="true"></i></button>',
                '<button class="ss-connector-btn"><i class="fa fa-long-arrow-right" aria-hidden="true"></i></button>',
                '</div>',
                '</div>'
            ].join(''),

            initialize: function () {
                _.bindAll(this, 'updateBox');
                jointWindow.dia.ElementView.prototype.initialize.apply(this, arguments);

                this.$box = $(_.template(this.template)());

                // This is an example of reacting on the input change and storing the input data in the cell model.
                this.$box.find('.ss-remove-btn').on('click', _.bind(this.model.remove, this.model));

                // Update the box position whenever the underlying model changes.
                this.model.on('change', this.updateBox, this);
                // Remove the box when the model gets removed from the graph.
                this.model.on('remove', this.removeBox, this);

                this.updateBox();
            },
            render: function () {
                jointWindow.dia.ElementView.prototype.render.apply(this, arguments);
                this.paper.$el.prepend(this.$box);
                this.updateBox();
                return this;
            },
            updateBox: function () {
                // Set the position and dimension of the box so that it covers the JointJS element.
                var bbox = this.model.getBBox();
                var zIndex = this.model.get('z');
                var ssName = this.$box.find('.ss-name');
                var ssIconBox = this.$box.find('.ss-icon-box');
                var ssIcon = this.$box.find('.ss-icon');
                var ssStartIcon = this.$box.find('.ss-start-icon');
                var ssEndIcon = this.$box.find('.ss-end-icon');

                // Example of updating the HTML with a data stored in the cell model.
                ssName.text(shorten(this.model.get('customAttrs').name, 50));

                if (this.model.get('customAttrs').startState) {
                    ssStartIcon.css('visibility', 'visible');
                    ssEndIcon.css('visibility', 'hidden');
                } else if (this.model.get('customAttrs').endState) {
                    ssStartIcon.css('visibility', 'hidden');
                    ssEndIcon.css('visibility', 'visible');
                } else {
                    ssStartIcon.css('visibility', 'hidden');
                    ssEndIcon.css('visibility', 'hidden');
                }

                this.$box.css({
                    width: bbox.width + 10,
                    height: bbox.height + 10,
                    left: bbox.x - 5,
                    top: bbox.y - 5,
                    'z-index': zIndex
                });

                this.$box.find('.send-state-element-inner').css({
                    position: 'fixed',
                    left: bbox.x,
                    top: bbox.y
                });

                ssName.css({
                    position: 'fixed',
                    left: bbox.x + (bbox.width / 2) - (ssName.width() / 2),
                    top: bbox.y + (bbox.height / 2) - 10
                });

                ssIconBox.css({
                    position: 'fixed',
                    left: bbox.x + 10,
                    top: bbox.y + 10
                });

                ssIcon.css({
                    position: 'fixed',
                    left: bbox.x + 15,
                    top: bbox.y + 22
                });

                ssStartIcon.css({
                    position: 'fixed',
                    left: bbox.x + 170,
                    top: bbox.y
                });

                ssEndIcon.css({
                    position: 'fixed',
                    left: bbox.x + 170,
                    top: bbox.y
                });
            },
            removeBox: function (evt) {
                this.$box.remove();
            }
        });

        //Create new SendStateElement
        service.sendStateElement = function (options) {
            options = options || service.sendStateElementDefaults;
            return new jointWindow.shapes.sbd.SendStateElement(options);
        };

        //
        //ReceiveStateElement
        //
        jointWindow.shapes.sbd.ReceiveStateElement = jointWindow.shapes.basic.Rect.extend({
            defaults: jointWindow.util.deepSupplement({
                type: 'sbd.ReceiveStateElement'
            }, jointWindow.shapes.basic.Rect.prototype.defaults)
        });

        //Custom view for ReceiveStateElement that displays an HTML div above it
        jointWindow.shapes.sbd.ReceiveStateElementView = jointWindow.dia.ElementView.extend({

            template: [
                '<div class="receive-state-element">',
                '<div class="receive-state-element-inner">',
                '<div class="rs-icon-box">',
                '<div class="rs-icon"></div>',
                '<div class="rs-start-icon"><i class="fa fa-play-circle-o" aria-hidden="true"></i></div>',
                '<div class="rs-end-icon"><i class="fa fa-stop-circle-o" aria-hidden="true"></i></div>',
                '</div>',
                '<span class="rs-name"></span>',
                '<button class="rs-remove-btn"><i class="fa fa-trash-o" aria-hidden="true"></i></button>',
                '<button class="rs-connector-btn"><i class="fa fa-long-arrow-right" aria-hidden="true"></i></button>',
                '</div>',
                '</div>'
            ].join(''),

            initialize: function () {
                _.bindAll(this, 'updateBox');
                jointWindow.dia.ElementView.prototype.initialize.apply(this, arguments);

                this.$box = $(_.template(this.template)());

                // This is an example of reacting on the input change and storing the input data in the cell model.
                this.$box.find('.rs-remove-btn').on('click', _.bind(this.model.remove, this.model));

                // Update the box position whenever the underlying model changes.
                this.model.on('change', this.updateBox, this);
                // Remove the box when the model gets removed from the graph.
                this.model.on('remove', this.removeBox, this);

                this.updateBox();
            },
            render: function () {
                jointWindow.dia.ElementView.prototype.render.apply(this, arguments);
                this.paper.$el.prepend(this.$box);
                this.updateBox();
                return this;
            },
            updateBox: function () {
                // Set the position and dimension of the box so that it covers the JointJS element.
                var bbox = this.model.getBBox();
                var zIndex = this.model.get('z');
                var rsName = this.$box.find('.rs-name');
                var rsIconBox = this.$box.find('.rs-icon-box');
                var rsIcon = this.$box.find('.rs-icon');
                var rsStartIcon = this.$box.find('.rs-start-icon');
                var rsEndIcon = this.$box.find('.rs-end-icon');

                // Example of updating the HTML with a data stored in the cell model.
                rsName.text(shorten(this.model.get('customAttrs').name, 50));

                if (this.model.get('customAttrs').startState) {
                    rsStartIcon.css('visibility', 'visible');
                    rsEndIcon.css('visibility', 'hidden');
                } else if (this.model.get('customAttrs').endState) {
                    rsStartIcon.css('visibility', 'hidden');
                    rsEndIcon.css('visibility', 'visible');
                } else {
                    rsStartIcon.css('visibility', 'hidden');
                    rsEndIcon.css('visibility', 'hidden');
                }

                this.$box.css({
                    width: bbox.width + 10,
                    height: bbox.height + 10,
                    left: bbox.x - 5,
                    top: bbox.y - 5,
                    'z-index': zIndex
                });

                this.$box.find('.receive-state-element-inner').css({
                    position: 'fixed',
                    left: bbox.x,
                    top: bbox.y
                });

                rsName.css({
                    position: 'fixed',
                    left: bbox.x + (bbox.width / 2) - (rsName.width() / 2),
                    top: bbox.y + (bbox.height / 2) - 10
                });

                rsIconBox.css({
                    position: 'fixed',
                    left: bbox.x + 10,
                    top: bbox.y + 16
                });

                rsIcon.css({
                    position: 'fixed',
                    left: bbox.x + 15,
                    top: bbox.y + 10
                });

                rsStartIcon.css({
                    position: 'fixed',
                    left: bbox.x + 170,
                    top: bbox.y
                });

                rsEndIcon.css({
                    position: 'fixed',
                    left: bbox.x + 170,
                    top: bbox.y
                });
            },
            removeBox: function (evt) {
                this.$box.remove();
            }
        });

        //Create new ReceiveStateElement
        service.receiveStateElement = function (options) {
            options = options || service.receiveStateElementDefaults;
            return new jointWindow.shapes.sbd.ReceiveStateElement(options);
        };

        //
        //ReceiveStateElement
        //
        jointWindow.shapes.sbd.FunctionStateElement = jointWindow.shapes.basic.Rect.extend({
            defaults: jointWindow.util.deepSupplement({
                type: 'sbd.FunctionStateElement'
            }, jointWindow.shapes.basic.Rect.prototype.defaults)
        });

        //Custom view for ReceiveStateElement that displays an HTML div above it
        jointWindow.shapes.sbd.FunctionStateElementView = jointWindow.dia.ElementView.extend({

            template: [
                '<div class="function-state-element">',
                '<div class="function-state-element-inner">',
                '<div class="fs-icon-box">',
                '<div class="fs-icon"></div>',
                '<div class="fs-start-icon"><i class="fa fa-play-circle-o" aria-hidden="true"></i></div>',
                '<div class="fs-end-icon"><i class="fa fa-stop-circle-o" aria-hidden="true"></i></div>',
                '</div>',
                '<span class="fs-name"></span>',
                '<button class="fs-remove-btn"><i class="fa fa-trash-o" aria-hidden="true"></i></button>',
                '<button class="fs-connector-btn"><i class="fa fa-long-arrow-right" aria-hidden="true"></i></button>',
                '</div>',
                '</div>'
            ].join(''),

            initialize: function () {
                _.bindAll(this, 'updateBox');
                jointWindow.dia.ElementView.prototype.initialize.apply(this, arguments);

                this.$box = $(_.template(this.template)());

                // This is an example of reacting on the input change and storing the input data in the cell model.
                this.$box.find('.fs-remove-btn').on('click', _.bind(this.model.remove, this.model));

                // Update the box position whenever the underlying model changes.
                this.model.on('change', this.updateBox, this);
                // Remove the box when the model gets removed from the graph.
                this.model.on('remove', this.removeBox, this);

                this.updateBox();
            },
            render: function () {
                jointWindow.dia.ElementView.prototype.render.apply(this, arguments);
                this.paper.$el.prepend(this.$box);
                this.updateBox();
                return this;
            },
            updateBox: function () {
                // Set the position and dimension of the box so that it covers the JointJS element.
                var bbox = this.model.getBBox();
                var zIndex = this.model.get('z');
                var fsName = this.$box.find('.fs-name');
                var fsIconBox = this.$box.find('.fs-icon-box');
                var fsIcon = this.$box.find('.fs-icon');
                var fsStartIcon = this.$box.find('.fs-start-icon');
                var fsEndIcon = this.$box.find('.fs-end-icon');

                // Example of updating the HTML with a data stored in the cell model.
                fsName.text(shorten(this.model.get('customAttrs').name, 50));

                if (this.model.get('customAttrs').startState) {
                    fsStartIcon.css('visibility', 'visible');
                    fsEndIcon.css('visibility', 'hidden');
                } else if (this.model.get('customAttrs').endState) {
                    fsStartIcon.css('visibility', 'hidden');
                    fsEndIcon.css('visibility', 'visible');
                } else {
                    fsStartIcon.css('visibility', 'hidden');
                    fsEndIcon.css('visibility', 'hidden');
                }

                this.$box.css({
                    width: bbox.width + 10,
                    height: bbox.height + 10,
                    left: bbox.x - 5,
                    top: bbox.y - 5,
                    'z-index': zIndex
                });

                this.$box.find('.function-state-element-inner').css({
                    position: 'fixed',
                    left: bbox.x,
                    top: bbox.y
                });

                fsName.css({
                    position: 'fixed',
                    left: bbox.x + (bbox.width / 2) - (fsName.width() / 2),
                    top: bbox.y + (bbox.height / 2) - 10
                });

                fsIconBox.css({
                    position: 'fixed',
                    left: bbox.x + 10,
                    top: bbox.y + 15
                });

                fsIcon.css({
                    position: 'fixed',
                    left: bbox.x + 25,
                    top: bbox.y + 10
                });

                fsStartIcon.css({
                    position: 'fixed',
                    left: bbox.x + 170,
                    top: bbox.y
                });

                fsEndIcon.css({
                    position: 'fixed',
                    left: bbox.x + 170,
                    top: bbox.y
                });
            },
            removeBox: function (evt) {
                this.$box.remove();
            }
        });

        //Create new FunctionStateElement
        service.functionStateElement = function (options) {
            options = options || service.functionStateElementDefaults;
            return new jointWindow.shapes.sbd.FunctionStateElement(options);
        };

        //
        //FromSendStateConnectorElement
        //
        jointWindow.shapes.sbd.FromSendStateConnectorElement = jointWindow.dia.Link.extend({
            defaults: jointWindow.util.deepSupplement({
                type: 'sbd.FromSendStateConnectorElement'
            }, jointWindow.dia.Link.prototype.defaults)
        });

        //Custom view for FromSendStateConnectorElement that displays an HTML div above it
        jointWindow.shapes.sbd.FromSendStateConnectorElementView = jointWindow.dia.LinkView.extend({

            template: [
                '<div class="from-send-state-connector-element">',
                '<div class="from-send-state-connector-element-inner">',
                '<div class="fss-subject"></div>',
                '<div class="fss-message"></div>',
                '<button class="fss-remove-btn"><i class="fa fa-trash-o" aria-hidden="true"></i></button>',
                '</div>',
                '</div>'
            ].join(''),

            initialize: function () {
                _.bindAll(this, 'updateConnector');
                jointWindow.dia.LinkView.prototype.initialize.apply(this, arguments);

                this.$box = $(_.template(this.template)());

                this.$box.on('click', _.bind(this.onClick, this));

                // This is an example of reacting on the input change and storing the input data in the cell model.
                this.$box.find('.fss-remove-btn').on('click', _.bind(this.model.remove, this.model));

                // Update the box position whenever the underlying model changes.
                this.model.on('change', this.updateConnector, this);
                // Remove the box when the model gets removed from the graph.
                this.model.on('remove', this.removeBox, this);

                this.updateConnector();
            },
            onClick: function (evt) {
                var p = this.paper.snapToGrid({
                    x: evt.originalEvent.x,
                    y: evt.originalEvent.y
                });
                this.pointerdown(evt, p.x, p.y);
                this.model.attributes.vertices = [];
                this.update();
                this.updateConnector();
            },
            render: function () {
                jointWindow.dia.LinkView.prototype.render.apply(this, arguments);
                this.paper.$el.prepend(this.$box);
                this.updateConnector();
                return this;
            },
            updateConnector: function () {
                var bbox = this.getBBox();
                var zIndex = this.model.get('z');

                this.$box.find('.fss-subject').text(this.model.get('customAttrs').toSubject.display ?
                'To: ' + this.model.get('customAttrs').toSubject.display : '');
                this.$box.find('.fss-message').text(this.model.get('customAttrs').toMessage.name);

                var position = {x: 0, y: 0};
                if (typeof this._V !== 'undefined') {
                    var connectionEl = this.$('.connection')[0];
                    var connectionLength = connectionEl.getTotalLength();
                    position = connectionEl.getPointAtLength(connectionLength / 2);
                }

                this.$box.css({
                    width: 210,
                    height: 90,
                    //left: position.x + (bbox.width / 2) - 80,
                    //top: position.y + (bbox.height / 2) - 30,
                    left: position.x - 105,
                    top: position.y - 45,
                    'z-index': zIndex
                });

                this.$box.find('.from-send-state-connector-element-inner').css({
                    position: 'fixed',
                    left: position.x - 100,
                    top: position.y - 40
                });
            },
            removeBox: function (evt) {
                this.$box.remove();
            }
        });

        //Create new FromSendStateConnectorElement
        service.fromSendStateConnectorElement = function (options) {
            //options = options || service.subjectElementDefaults;
            return new jointWindow.shapes.sbd.FromSendStateConnectorElement(options);
        };

        //
        //FromReceiveStateConnectorElement
        //
        jointWindow.shapes.sbd.FromReceiveStateConnectorElement = jointWindow.dia.Link.extend({
            defaults: jointWindow.util.deepSupplement({
                type: 'sbd.FromReceiveStateConnectorElement'
            }, jointWindow.dia.Link.prototype.defaults)
        });

        //Custom view for FromReceiveStateConnectorElement that displays an HTML div above it
        jointWindow.shapes.sbd.FromReceiveStateConnectorElementView = jointWindow.dia.LinkView.extend({

            template: [
                '<div class="from-receive-state-connector-element">',
                '<div class="from-receive-state-connector-element-inner">',
                '<div class="frs-subject"></div>',
                '<div class="frs-message"></div>',
                '<button class="frs-remove-btn"><i class="fa fa-trash-o" aria-hidden="true"></i></button>',
                '</div>',
                '</div>'
            ].join(''),

            initialize: function () {
                _.bindAll(this, 'updateConnector');
                jointWindow.dia.LinkView.prototype.initialize.apply(this, arguments);

                this.$box = $(_.template(this.template)());

                this.$box.on('click', _.bind(this.onClick, this));

                // This is an example of reacting on the input change and storing the input data in the cell model.
                this.$box.find('.frs-remove-btn').on('click', _.bind(this.model.remove, this.model));

                // Update the box position whenever the underlying model changes.
                this.model.on('change', this.updateConnector, this);
                // Remove the box when the model gets removed from the graph.
                this.model.on('remove', this.removeBox, this);

                this.updateConnector();
            },
            onClick: function (evt) {
                var p = this.paper.snapToGrid({
                    x: evt.originalEvent.x,
                    y: evt.originalEvent.y
                });
                this.pointerdown(evt, p.x, p.y);
                this.model.attributes.vertices = [];
                this.update();
                this.updateConnector();
            },
            render: function () {
                jointWindow.dia.LinkView.prototype.render.apply(this, arguments);
                this.paper.$el.prepend(this.$box);
                this.updateConnector();
                return this;
            },
            updateConnector: function () {
                var bbox = this.getBBox();
                var zIndex = this.model.get('z');

                this.$box.find('.frs-subject').text(this.model.get('customAttrs').fromSubject.display ?
                'From: ' + this.model.get('customAttrs').fromSubject.display : '');
                this.$box.find('.frs-message').text(this.model.get('customAttrs').fromMessage.name);

                var position = {x: 0, y: 0};
                if (typeof this._V !== 'undefined') {
                    var connectionEl = this.$('.connection')[0];
                    var connectionLength = connectionEl.getTotalLength();
                    position = connectionEl.getPointAtLength(connectionLength / 2);
                }

                this.$box.css({
                    width: 210,
                    height: 90,
                    //left: position.x + (bbox.width / 2) - 80,
                    //top: position.y + (bbox.height / 2) - 30,
                    left: position.x - 105,
                    top: position.y - 45,
                    'z-index': zIndex
                });

                this.$box.find('.from-receive-state-connector-element-inner').css({
                    position: 'fixed',
                    left: position.x - 100,
                    top: position.y - 40
                });
            },
            removeBox: function (evt) {
                this.$box.remove();
            }
        });

        //Create new FromReceiveStateConnectorElement
        service.fromReceiveStateConnectorElement = function (options) {
            //options = options || service.subjectElementDefaults;
            return new jointWindow.shapes.sbd.FromReceiveStateConnectorElement(options);
        };

        //
        //FromFunctionStateConnectorElement
        //
        jointWindow.shapes.sbd.FromFunctionStateConnectorElement = jointWindow.dia.Link.extend({
            defaults: jointWindow.util.deepSupplement({
                type: 'sbd.FromFunctionStateConnectorElement'
            }, jointWindow.dia.Link.prototype.defaults)
        });

        //Custom view for FromFunctionStateConnectorElement that displays an HTML div above it
        jointWindow.shapes.sbd.FromFunctionStateConnectorElementView = jointWindow.dia.LinkView.extend({

            template: [
                '<div class="from-function-state-connector-element">',
                '<div class="from-function-state-connector-element-inner">',
                '<div class="ffs-name"></div>',
                '<button class="ffs-remove-btn"><i class="fa fa-trash-o" aria-hidden="true"></i></button>',
                '</div>',
                '</div>'
            ].join(''),

            initialize: function () {
                _.bindAll(this, 'updateConnector');
                jointWindow.dia.LinkView.prototype.initialize.apply(this, arguments);

                this.$box = $(_.template(this.template)());

                this.$box.on('click', _.bind(this.onClick, this));

                // This is an example of reacting on the input change and storing the input data in the cell model.
                this.$box.find('.ffs-remove-btn').on('click', _.bind(this.model.remove, this.model));

                // Update the box position whenever the underlying model changes.
                this.model.on('change', this.updateConnector, this);
                // Remove the box when the model gets removed from the graph.
                this.model.on('remove', this.removeBox, this);

                this.updateConnector();
            },
            onClick: function (evt) {
                var p = this.paper.snapToGrid({
                    x: evt.originalEvent.x,
                    y: evt.originalEvent.y
                });
                this.pointerdown(evt, p.x, p.y);
                this.model.attributes.vertices = [];
                this.update();
                this.updateConnector();
            },
            render: function () {
                jointWindow.dia.LinkView.prototype.render.apply(this, arguments);
                this.paper.$el.prepend(this.$box);
                this.updateConnector();
                return this;
            },
            updateConnector: function () {
                var bbox = this.getBBox();
                var zIndex = this.model.get('z');

                this.$box.find('.ffs-name').text(this.model.get('customAttrs').transitionName);

                var position = {x: 0, y: 0};
                if (typeof this._V !== 'undefined') {
                    var connectionEl = this.$('.connection')[0];
                    var connectionLength = connectionEl.getTotalLength();
                    position = connectionEl.getPointAtLength(connectionLength / 2);
                }

                this.$box.css({
                    width: 210,
                    height: 90,
                    //left: position.x + (bbox.width / 2) - 80,
                    //top: position.y + (bbox.height / 2) - 30,
                    left: position.x - 105,
                    top: position.y - 45,
                    'z-index': zIndex
                });

                this.$box.find('.from-function-state-connector-element-inner').css({
                    position: 'fixed',
                    left: position.x - 100,
                    top: position.y - 40
                });
            },
            removeBox: function (evt) {
                this.$box.remove();
            }
        });

        //Create new FromFunctionStateConnectorElement
        service.fromFunctionStateConnectorElement = function (options) {
            //options = options || service.subjectElementDefaults;
            return new jointWindow.shapes.sbd.FromFunctionStateConnectorElement(options);
        };

        var shorten = function(text, maxLength) {
            var ret = text;
            if (ret.length > maxLength) {
                ret = ret.substr(0,maxLength-3) + "...";
            }
            return ret;
        };

        service.init();

        return service;
    }

})();
