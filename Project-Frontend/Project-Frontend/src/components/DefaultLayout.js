import React, { Component, Suspense } from 'react';
import { Redirect, Route,  } from 'react-router-dom';
import * as router from 'react-router-dom';
import { Container } from 'reactstrap';

import {
  AppAside,
  CFooter,
  CHeader,
  CSidebar,
  CSidebarFooter,
  AppSidebarForm,
  CSidebarHeader,
  AppSidebarMinimizer,
  CBreadcrumb,
  CSidebarNav,
} from '@coreui/react';
// sidebar nav config
import navigation from '../_nav';          



// routes config
import routes from '../routes';
import DefaultFooter from './DefaultFooter';
import DefaultHeader from './DefaultHeader';
import DefaultAside from './DefaultAside'


class DefaultLayout extends Component {

  loading = () => <div className="animated fadeIn pt-1 text-center">Loading...</div>

  signOut(e) {
    e.preventDefault()
    this.props.history.push('/login')
  }

  render() {
    return (
      <div className="app">
        <CHeader fixed>
          <Suspense  fallback={this.loading()}>
            <DefaultHeader onLogout={e=>this.signOut(e)}/>
          </Suspense>
        </CHeader>
        <div className="app-body">
          <CSidebar fixed display="lg">
            <CSidebarHeader />
            {/* <AppSidebarForm /> */}
            <Suspense>
            <CSidebarNav navConfig={navigation} {...this.props} router={router}/>
            </Suspense>
            <CSidebarFooter />
            {/* <AppSidebarMinimizer /> */}
          </CSidebar>
          <main className="main">
            <CBreadcrumb appRoutes={routes} router={router}/>
            <Container fluid>
              <Suspense fallback={this.loading()}>
               
                  {routes.map((route, idx) => {
                    return route.component ? (
                      <Route
                        key={idx}
                        path={route.path}
                        exact={route.exact}
                        name={route.name}
                        render={props => (
                          <route.component {...props} />
                        )} />
                    ) : (null);
                  })}
                  <Redirect from="/" to="/dashboard" />
                
              </Suspense>
            </Container>
          </main>
          {/* <AppAside fixed>
            <Suspense fallback={this.loading()}>
              <DefaultAside />
            </Suspense>
          </AppAside> */}
        </div>
        <CFooter>
          <Suspense fallback={this.loading()}>
            <DefaultFooter />
          </Suspense>
        </CFooter>
      </div>
    );
  }
}

export default DefaultLayout;
