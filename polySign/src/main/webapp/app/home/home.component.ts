import { Component, OnInit } from '@angular/core';

import { LoginService } from 'app/core/login/login.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
// import {Router} from "@angular/router";
// import {FormBuilder} from "@angular/forms";
import { Subscription } from 'rxjs';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss'],
})
export class HomeComponent implements OnInit {
  account: Account | null = null;
  authSubscription?: Subscription;
  authenticationError = false;
  isAuth = false;
  /* loginForm = this.formBuilder.group({
    username: [''],
    password: [''],
    rememberMe: [false],
  });*/

  constructor(private accountService: AccountService, private loginService: LoginService) // private router : Router,
  // private formBuilder : FormBuilder
  {}

  ngOnInit(): void {
    // this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    this.accountService.identity().subscribe(account => (this.account = account));
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginService.login();
  }
}
