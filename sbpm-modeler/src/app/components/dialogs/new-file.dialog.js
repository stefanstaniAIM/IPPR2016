(function () {
    'use strict';

    angular
        .module('sbpm-modeler')
        .factory('newFileDialog', newFileDialog);

    /** @ngInject */
    function newFileDialog($mdDialog, modeler, $window) {

        var TAG = 'new-file.dialog: ';

        return {
            showDialog: function () {
                var confirm = $mdDialog.confirm()
                    .title('Are you sure?')
                    .textContent('Everything will be deleted permanently.')
                    .ok('Confirm')
                    .cancel('Cancel');

                $mdDialog.show(confirm)
                    .then(function () {
                        modeler.clear().then(function () {
                            $window.location.reload();
                        });
                    })
                    .catch(function () {

                    });
            }
        };

    }

})();
