(function() {

  'use strict';

  angular.module('ui.fabric')
    .service('fabricUtils', fabricUtils);

  /** @ngInject */
  function fabricUtils($log) {

      var TAG = 'ui-fabric-utils.service: ';

    var service = this;

    service.init = function () {
      $log.debug(TAG + 'init()');
    };

    // See: _findTargetCorner()

    service.findTargetPort = function(port, object) {

      var points = new Array(4);

      $log.debug(TAG + 'findTargetPort() - port: ' + port);

      switch (port) {

        case 'mt':
          points = [
            object.left + (object.width / 2), object.top,
            object.left + (object.width / 2), object.top
          ];
          break;
        case 'mr':
          points = [
            object.left + object.width, object.top + (object.height / 2),
            object.left + object.width, object.top + (object.height / 2)
          ];
          break;
        case 'mb':
          points = [
            object.left + (object.width / 2), object.top + object.height,
            object.left + (object.width / 2), object.top + object.height
          ];
          break;
        case 'ml':
          points = [
            object.left, object.top + (object.height / 2),
            object.left, object.top + (object.height / 2)
          ];
          break;

        default:
          $log.error(TAG + 'findTargetPort() - port === undefined');
          break;
      }

      return points

    };

    // Clockwise

    service.getNextTargetPort = function(port) {

      $log.debug(TAG + 'getNextTargetPort() - port: ' + port);

      var nextPort = 'mt';

      switch (port) {
        case 'mt':
          nextPort = 'mr';
          break;
        case 'mr':
          nextPort = 'mb';
          break;
        case 'mb':
          nextPort = 'ml';
          break;
        case 'ml':
          nextPort = 'mt';
          break;
        default:
          $log.error(TAG + 'getNextTargetPort() - port === undefined');
          break;
      }

      $log.debug(TAG + 'getNextTargetPort() - port: ' + port + ' nextPort: ' + nextPort);

      return nextPort;

    };

    service.getPortCenterPoint = function(object, port) {

      $log.debug(TAG + 'getPortCenterPoint() - port: ' + port);

      var x1 = 0;
      var y1 = 0;

      switch (port) {

        case 'mt':
          x1 = object.left + (object.width / 2);
          y1 = object.top;
          break;

        case 'mr':
          x1 = object.left + object.width;
          y1 = object.top + (object.height / 2);
          break;

        case 'mb':
          x1 = object.left + (object.width / 2);
          y1 = object.top + object.height;
          break;
        case 'ml':
          x1 = object.left;
          y1 = object.top + (object.height / 2);
          break;

        default:
          $log.error(TAG + 'getPortCenterPoint() - port === undefined');
          break;
      }

      return {
        'x1': x1, 'y1': y1,
        'x2': x1, 'y2': y1
      }
    };

    service.init();

    return service;

  }

})();

/*


return {
  x1: x1, y1: y1,
  x2: x1, y2: y1
}

return {
  'x1': x1, 'y1': y1,
  'x2': x1, 'y2': y1
}

 // $log.debug('x1: ' + x1 + ' y1: ' + y1 + ' x2: ' + x2 + ' y2: ' + y2);

*/


