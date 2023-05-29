import { AuthenticationService } from './../services/authentication.service';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { HotToastService } from '@ngneat/hot-toast';
import { Router } from '@angular/router';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required]),
  });
  user: string | null = 'jm';
  showErrorAlert: Boolean = false;
  showSuccessAlert: Boolean = false;
  constructor(
    private auth: AuthenticationService,
    private toast: HotToastService,
    private router: Router
  ) {
    this.toast.defaultConfig = {
      ...this.toast.defaultConfig,
      reverseOrder: true,
      dismissible: true,
      autoClose: false,
    };
  }
  ngOnInit(): void {}

  loginFunc() {
    if (!this.loginForm.valid) {
      return;
    }
    const { email, password } = this.loginForm.value;
    this.auth
      .login(email!, password!)
      .pipe(
        this.toast.observe({
          loading: 'Logging in....',
          error: {
            content: 'Invalid email or password',
            duration: 1000,
            autoClose: true,
            dismissible: true,
          },
          success: {
            content: 'Invalid email or password',
            duration: 1000,
            autoClose: true,
            dismissible: true,
          },
        })
      )
      .subscribe((result: any) => {
        this.router.navigate(['main']);
      });
  }
}
