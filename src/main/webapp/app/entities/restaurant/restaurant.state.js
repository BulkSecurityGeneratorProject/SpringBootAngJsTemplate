(function() {
    'use strict';

    angular
        .module('springBootAngJsTemplateApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('restaurant', {
            parent: 'entity',
            url: '/restaurant',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'springBootAngJsTemplateApp.restaurant.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/restaurant/restaurants.html',
                    controller: 'RestaurantController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('restaurant');
                    $translatePartialLoader.addPart('statusEnum');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('restaurant-detail', {
            parent: 'restaurant',
            url: '/restaurant/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'springBootAngJsTemplateApp.restaurant.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/restaurant/restaurant-detail.html',
                    controller: 'RestaurantDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('restaurant');
                    $translatePartialLoader.addPart('statusEnum');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Restaurant', function($stateParams, Restaurant) {
                    return Restaurant.get({id : $stateParams.id}).$promise;
                }],
                pics: ['$stateParams', 'PictureRest', function($stateParams, PictureRest) {
                    var data = {mode: 'getPicRes', resId : $stateParams.id};
                    return PictureRest.getList({json: angular.toJson(data)}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'restaurant',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('restaurant-detail.edit', {
            parent: 'restaurant-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/restaurant/restaurant-dialog.html',
                    controller: 'RestaurantDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Restaurant', function(Restaurant) {
                            return Restaurant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('restaurant.new', {
            parent: 'restaurant',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/restaurant/restaurant-dialog.html',
                    controller: 'RestaurantDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('restaurant', null, { reload: 'restaurant' });
                }, function() {
                    $state.go('restaurant');
                });
            }]
        })
        .state('restaurant.edit', {
            parent: 'restaurant',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/restaurant/restaurant-dialog.html',
                    controller: 'RestaurantDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Restaurant', function(Restaurant) {
                            return Restaurant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('restaurant', null, { reload: 'restaurant' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('restaurant.delete', {
            parent: 'restaurant',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/restaurant/restaurant-delete-dialog.html',
                    controller: 'RestaurantDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Restaurant', function(Restaurant) {
                            return Restaurant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('restaurant', null, { reload: 'restaurant' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
