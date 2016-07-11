(function() {
    'use strict';

    angular
        .module('rentApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('house', {
            parent: 'entity',
            url: '/house',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Houses'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/house/houses.html',
                    controller: 'HouseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('house-detail', {
            parent: 'entity',
            url: '/house/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'House'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/house/house-detail.html',
                    controller: 'HouseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'House', function($stateParams, House) {
                    return House.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('house.new', {
            parent: 'house',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/house/house-dialog.html',
                    controller: 'HouseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                houseId: null,
                                houseType: null,
                                houseFor: null,
                                noOfRooms: null,
                                foodPreference: null,
                                rentToBePaid: null,
                                rules: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('house', null, { reload: true });
                }, function() {
                    $state.go('house');
                });
            }]
        })
        .state('house.edit', {
            parent: 'house',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/house/house-dialog.html',
                    controller: 'HouseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['House', function(House) {
                            return House.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('house', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('house.delete', {
            parent: 'house',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/house/house-delete-dialog.html',
                    controller: 'HouseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['House', function(House) {
                            return House.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('house', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
