import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PolySignSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { UserHomeComponent } from 'app/user-home/user-home.component';

@NgModule({
  imports: [PolySignSharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent, UserHomeComponent],
})
export class PolySignHomeModule {}
