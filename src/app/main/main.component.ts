import { HotToastService } from '@ngneat/hot-toast';
import { Router } from '@angular/router';
import { Component } from '@angular/core';
import { AuthenticationService } from '../services/authentication.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css'],
})
export class MainComponent {
  user = this.auth.currentUser;
  constructor(
    private router: Router,
    private auth: AuthenticationService,
    private toast: HotToastService
  ) {}
  logoutFunc() {
    this.auth
      .logout()
      .pipe(
        this.toast.observe({
          loading: 'Logging out...',
          success: 'Successfully logged out!',
          error: 'Error Logging out!',
        })
      )
      .subscribe(() => {
        this.router.navigate(['']);
        this.toast.close();
      })
      .unsubscribe();
  }
}
