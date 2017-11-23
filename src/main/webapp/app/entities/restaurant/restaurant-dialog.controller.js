(function() {
    'use strict';

    angular
        .module('springBootAngJsTemplateApp')
        .controller('RestaurantDialogController', RestaurantDialogController);

    RestaurantDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Restaurant', 'Customer', 'Picture'];

    function RestaurantDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Restaurant, Customer, Picture) {
        var vm = this;

        vm.restaurant = entity;
        vm.clear = clear;
        vm.save = save;
        vm.customers = Customer.query();
        vm.pictures = Picture.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.restaurant.id !== null) {
                Restaurant.update(vm.restaurant, onSaveSuccess, onSaveError);
            } else {
                Restaurant.save(vm.restaurant, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('springBootAngJsTemplateApp:restaurantUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
