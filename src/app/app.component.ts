import { Component, OnInit } from '@angular/core';
import { Auth, authState } from '@angular/fire/auth';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  title = 'Lets Meat';
  user = this.auth.currentUser;

  constructor(private router: Router, private auth: Auth) {}
  ngOnInit(): void {
    if (this.user == undefined) {
      this.router.navigate(['/login']);
    } else {
      this.router.navigate(['/main']);
    }
  }
}
