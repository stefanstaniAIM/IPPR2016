(function() {
    'use strict';

    angular.module('ui.fabric')
        .service('fabricConfig', fabricConfig);

    /** @ngInject */
    function fabricConfig() {

        var service = this;

        const PORTRAIT_WIDTH = 1080;
        const PORTRAIT_HEIGHT = 1920;
        const LANDSCAPE_WIDTH = 1920;
        const LANDSCAPE_HEIGHT = 1080;

        const CANVAS_WIDTH = LANDSCAPE_WIDTH;
        const CANVAS_HEIGHT = LANDSCAPE_HEIGHT;

        const GRID_SIZE = 50;

        const CORNER_SIZE = 10;

        const STROKE_WIDTH = 1;

        const RECT_WIDTH = 400;
        const RECT_HEIGHT = 400;

        const FONT_SIZE = 12;
        const FONT_WEIGHT = 'normal';
        const FONT_FAMILY = 'Tahoma';

        const ARROW_HEAD_LENGTH = 15;

        var canvasDefaults = {
            backgroundColor: '#ffffff',
            height: CANVAS_HEIGHT,
            width: CANVAS_WIDTH,
            originalHeight: CANVAS_HEIGHT,
            originalWidth: CANVAS_WIDTH,
            selection: false
        };

        var objectDefaults = {
            borderColor: '#afb9ce',
            centerTransform: true,
            cornerColor: '#afb9ce',
            cornerSize: CORNER_SIZE,
            hasBorders: true,
            hasRotatingPoint: false,
            padding: 10,
            selectable: true,
            transparentCorners: true,
            borderScaleFactor: 2,
            hoverCursor: 'pointer',
            minScaleLimit: 1
        };

        var rectDefaults = angular.extend({
            fill: '#ffffff',
            stroke: '#000000',
            strokeWidth: 1,
            height: RECT_HEIGHT,
            width: RECT_WIDTH
        }, objectDefaults);

        var lineDefaults = angular.extend({
            stroke: 'BLACK',
            strokeWidth: STROKE_WIDTH
        }, objectDefaults);

        var triangleDefaults = angular.extend({
            // angle: angle,
            fill: 'BLUE',
            height: GRID_SIZE,
            left: GRID_SIZE,
            originX: 'center',
            originY: 'center',
            top: GRID_SIZE,
            width: GRID_SIZE
        }, objectDefaults);

        var textDefaults = angular.extend({
            fill: 'BLACK',
            fontFamily: FONT_FAMILY,
            fontSize: FONT_SIZE,
            fontWeight: FONT_WEIGHT,
            left: GRID_SIZE,
            originX: 'left',
            originY: 'top',
            scaleX: 1,
            scaleY: 1,
            textAlign: 'left',
            top: GRID_SIZE
        }, objectDefaults);

        var gridLineDefaults = {
            stroke: 'LIGHTGRAY'
        };

        var controlDefaults = {
            selectable: true,
            stroke: 'LIGHTBLUE',
            strokeWidth: STROKE_WIDTH
        };

        //
        // Connectors (a line) are not selectable (but the arrows on either end are).
        //

        var connectorDefaults = {
            selectable: false,
            stroke: 'BLACK',
            strokeWidth: 2 // STROKE_WIDTH
        };

        //
        // Arrows (a triangle)
        //

        var arrowDefaults = angular.extend({
            // angle: angle,
            fill: 'BLACK',
            hasControls: false,
            height: ARROW_HEAD_LENGTH,
            left: GRID_SIZE,
            originX: 'center',
            originY: 'center',
            top: GRID_SIZE,
            width: ARROW_HEAD_LENGTH
        }, objectDefaults);

        arrowDefaults.hasRotatingPoint = false;
        arrowDefaults.selectable = false;

        //
        // RectWithText (a Node or a Container)
        //

        // const RECT_WITH_TEXT_FONT_SIZE = 18;
        const RECT_WITH_TEXT_FONT_SIZE = '18';
        const RECT_WITH_TEXT_FONT_WEIGHT = 'normal';
        const RECT_WITH_TEXT_WIDTH = 100;
        const RECT_WITH_TEXT_HEIGHT = 100;

        var rectWithTextDefaults = angular.extend({
            fillStyle: 'BLACK',
            fontFamily: FONT_FAMILY,
            fontSize: RECT_WITH_TEXT_FONT_SIZE,  // option element can only hold string type as its value
            fontWeight: RECT_WITH_TEXT_FONT_WEIGHT,
            originX: 'left',
            originY: 'top',
            scaleX: 1,
            scaleY: 1,
            textXAlign: 'center',   // left, right, center, start, end
            textYAlign: 'middle',   // top, bottom, middle
            textBaseline: 'middle'  // top, bottom, middle, alphabetic, hanging
        }, rectDefaults);

        //
        // SubjectElement
        //

        const SUBJECT_ELEMENT_FONT_SIZE = '12';
        const SUBJECT_ELEMENT_FONT_WEIGHT = 'normal';
        const SUBJECT_ELEMENT_WIDTH = 100;
        const SUBJECT_ELEMENT_HEIGHT = 100;

        var subjectElementDefaults = angular.extend({
            fillStyle: '#000000',
            fontFamily: FONT_FAMILY,
            fontSize: SUBJECT_ELEMENT_FONT_SIZE,  // option element can only hold string type as its value
            fontWeight: SUBJECT_ELEMENT_FONT_WEIGHT,
            originX: 'left',
            originY: 'top',
            scaleX: 1,
            scaleY: 1,
            textXAlign: 'center',   // left, right, center, start, end
            textYAlign: 'middle',   // top, bottom, middle
            textBaseline: 'middle'  // top, bottom, middle, alphabetic, hanging
        }, rectDefaults);

        subjectElementDefaults.width = SUBJECT_ELEMENT_WIDTH;
        subjectElementDefaults.height = SUBJECT_ELEMENT_HEIGHT;

        service.getCanvasDefaults = function() {
            return canvasDefaults;
        };

        service.getLineDefaults = function() {
            return lineDefaults;
        };

        service.getRectDefaults = function() {
            return rectDefaults;
        };

        service.getTriangleDefaults = function() {
            return triangleDefaults;
        };

        service.getTextDefaults = function() {
            return textDefaults;
        };

        service.getGridLineDefaults = function() {
            return gridLineDefaults;
        };

        service.getControlDefaults = function() {
            return controlDefaults;
        };

        service.getConnectorDefaults = function() {
            return connectorDefaults;
        };

        service.getArrowDefaults = function() {
            return arrowDefaults;
        };

        service.getRectWithTextDefaults = function() {
            return rectWithTextDefaults;
        };

        service.getSubjectElementDefaults = function() {
            return subjectElementDefaults;
        };

        return service;

    }

})();
