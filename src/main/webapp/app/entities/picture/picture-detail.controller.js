(function() {
    'use strict';

    angular
        .module('springBootAngJsTemplateApp')
        .controller('PictureDetailController', PictureDetailController);

    PictureDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Picture', 'Restaurant'];

    function PictureDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Picture, Restaurant) {
        var vm = this;

        vm.picture = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('springBootAngJsTemplateApp:pictureUpdate', function(event, result) {
            vm.picture = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
