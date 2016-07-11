(function() {
    'use strict';

    angular
        .module('rentApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('country', {
            parent: 'entity',
            url: '/country',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Countries'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/country/countries.html',
                    controller: 'CountryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('country-detail', {
            parent: 'entity',
            url: '/country/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Country'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/country/country-detail.html',
                    controller: 'CountryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Country', function($stateParams, Country) {
                    return Country.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('country.new', {
            parent: 'country',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/country/country-dialog.html',
                    controller: 'CountryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                countryId: null,
                                country: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('country', null, { reload: true });
                }, function() {
                    $state.go('country');
                });
            }]
        })
        .state('country.edit', {
            parent: 'country',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/country/country-dialog.html',
                    controller: 'CountryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Country', function(Country) {
                            return Country.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('country', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('country.delete', {
            parent: 'country',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/country/country-delete-dialog.html',
                    controller: 'CountryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Country', function(Country) {
                            return Country.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('country', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
