(function () {
    'use strict';

    angular
        .module('sbpm-modeler')
        .factory('newFileDialog', newFileDialog);

    /** @ngInject */
    function newFileDialog($mdDialog, modeler, $window, fabric) {

        return {
            showDialog: function () {
                var confirm = $mdDialog.confirm()
                    .title('Are you sure?')
                    .textContent('Everything will be deleted permanently.')
                    .clickOutsideToClose(true)
                    .ok('Confirm')
                    .cancel('Cancel');

                $mdDialog.show(confirm)
                    .then(function () {
                        fabric.getCanvas().clear();
                        modeler.clear();
                        $window.location.reload();
                    })
                    .catch(function () {

                    });
            }
        };

    }

})();
