(function() {
    'use strict';

    angular
        .module('springBootAngJsTemplateApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('picture', {
            parent: 'entity',
            url: '/picture?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'springBootAngJsTemplateApp.picture.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/picture/pictures.html',
                    controller: 'PictureController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('picture');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('picture-detail', {
            parent: 'picture',
            url: '/picture/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'springBootAngJsTemplateApp.picture.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/picture/picture-detail.html',
                    controller: 'PictureDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('picture');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Picture', function($stateParams, Picture) {
                    return Picture.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'picture',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('picture-detail.edit', {
            parent: 'picture-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/picture/picture-dialog.html',
                    controller: 'PictureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Picture', function(Picture) {
                            return Picture.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('picture.new', {
            parent: 'picture',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/picture/picture-dialog.html',
                    controller: 'PictureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                img: null,
                                imgContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('picture', null, { reload: 'picture' });
                }, function() {
                    $state.go('picture');
                });
            }]
        })
        .state('picture.edit', {
            parent: 'picture',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/picture/picture-dialog.html',
                    controller: 'PictureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Picture', function(Picture) {
                            return Picture.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('picture', null, { reload: 'picture' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('picture.delete', {
            parent: 'picture',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/picture/picture-delete-dialog.html',
                    controller: 'PictureDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Picture', function(Picture) {
                            return Picture.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('picture', null, { reload: 'picture' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
