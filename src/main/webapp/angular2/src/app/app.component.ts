import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {Http, RequestOptions, Response, Headers} from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {Observable} from 'rxjs/Observable';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  private baseUrl: string = 'http://localhost:8080';
  public submitted: boolean;
  roomsearch: FormGroup;
  rooms: Room[];
  currentCheckInVal: string;
  currentCheckOutVal: string;
  currentShowParamVal: string;
  request: ReserveRoomRequest;
  bookingResultMessage: string;

  constructor(private http: Http) {}

  ngOnInit() {
    this.roomsearch = new FormGroup({
      checkin: new FormControl(''),
      checkout: new FormControl(''),
      showParam: new FormControl('availableOnly')
    });

    const roomsearchValueChanges$ = this.roomsearch.valueChanges;

    roomsearchValueChanges$.subscribe(valChange => {
      this.currentCheckInVal = valChange.checkin;
      this.currentCheckOutVal = valChange.checkout;
      this.currentShowParamVal = valChange.showParam;
    });
  }

  onSubmit({value, valid}: {value: Roomsearch, valid: boolean}) {
    this.getAll()
      .subscribe(
        rooms => this.rooms = rooms,
        err => {
          console.log(err);
        }
      );
  }

  reserveRoom(value: string) {
    this.request = new ReserveRoomRequest(value, this.currentCheckInVal, this.currentCheckOutVal);
    this.createReservation(this.request);
  }

  getAll(): Observable<Room[]> {
    //console.log('currentCheckInVal: ' + this.currentCheckInVal);
    //console.log('currentCheckOutVal: ' + this.currentCheckOutVal);

    return this.http.get(`${this.baseUrl}/room/reservation/v1
    ?checkin=${this.currentCheckInVal}
    &checkout=${this.currentCheckOutVal}
    &showParam=${this.currentShowParamVal}`)
      .map(this.mapRoom);

  }

  createReservation(body: ReserveRoomRequest) {
    let bodyString = JSON.stringify(body);
    let headers = new Headers({'Content-Type': 'application/json'});
    let option = new RequestOptions({headers: headers});


    let postRequest = this.http.post(this.baseUrl + '/room/reservation/v1', body, option);
    postRequest.map(res => res.json())
      .subscribe(res => {
        console.log(res);
        this.bookingResultMessage = `${res.resMsg}`;
    },
    err => {
      console.log(err);
      this.bookingResultMessage = `Unfortunately, the booking service is temporary unavailable`;
    }
    );

  }

  mapRoom(response: Response): Room[] {
    return response.json().content;
  }
}
export interface Roomsearch {
  checkin: string;
  checkout: string;
  showParam: string;
}

export class Room {
  id: string;
  roomNumber: string;
  price: string;
  links: string;

}

export class ReserveRoomRequest {
  roomId: string;
  checkin: string;
  checkout: string;
  constructor(
    roomId: string,
    checkin: string,
    checkout: string
  ) {
      this.roomId = roomId;
      this.checkin = checkin;
      this.checkout = checkout;
    }
}
