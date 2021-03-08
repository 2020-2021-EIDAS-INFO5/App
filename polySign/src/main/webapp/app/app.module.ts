import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { PolySignSharedModule } from 'app/shared/shared.module';
import { PolySignCoreModule } from 'app/core/core.module';
import { PolySignAppRoutingModule } from './app-routing.module';
import { PolySignHomeModule } from './home/home.module';
import { PolySignEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    PolySignSharedModule,
    PolySignCoreModule,
    PolySignHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    PolySignEntityModule,
    PolySignAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent],
})
export class PolySignAppModule {}
