(function() {
    'use strict';

    angular.module('uiJoint.jointWindow', [])
        .factory('jointWindow', jointWindow);

    /** @ngInject */
    function jointWindow($window) {
        return $window.joint;
    }

})();
