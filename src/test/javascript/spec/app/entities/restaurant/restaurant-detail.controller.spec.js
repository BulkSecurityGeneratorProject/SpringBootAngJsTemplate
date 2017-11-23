'use strict';

describe('Controller Tests', function() {

    describe('Restaurant Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockRestaurant, MockCustomer, MockPicture;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockRestaurant = jasmine.createSpy('MockRestaurant');
            MockCustomer = jasmine.createSpy('MockCustomer');
            MockPicture = jasmine.createSpy('MockPicture');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Restaurant': MockRestaurant,
                'Customer': MockCustomer,
                'Picture': MockPicture
            };
            createController = function() {
                $injector.get('$controller')("RestaurantDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'springBootAngJsTemplateApp:restaurantUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
