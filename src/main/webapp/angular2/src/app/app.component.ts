import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {Http, URLSearchParams, RequestOptions, Response, Headers} from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {Observable} from 'rxjs/Observable';
import {PaginationPage, PaginationPropertySort} from './pagination';
import {Table} from './table';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, Table<Room> {

  private baseUrl = 'http://localhost:8080';
  public submitted: boolean;
  roomsearch: FormGroup;
  roomsPage: PaginationPage<Room>;
  self: Table<Room>;
  isSelfSetup: boolean = false;
  checkinDate: Date;
  checkoutDate: Date;
  currentCheckInVal: string;
  currentCheckOutVal: string;
  currentShowParamVal: string;
  currentPageNumber: number;
  currentPageSize: number;
  currentSort: PaginationPropertySort;

  reservedRoomsBuffer: Array<number>;

  request: ReserveRoomRequest;
  bookingResultMessage: string;


  constructor(private http: Http) {}

  ngOnInit() {
    this.roomsearch = new FormGroup({
      checkin: new FormControl(''),
      checkout: new FormControl(''),
      showParam: new FormControl('availableOnly'),
      sortBy: new FormControl('price,asc'),
      pageSize: new FormControl('10')
    });
    // Setting the default values
    this.checkinDate = new Date();
    this.checkoutDate = new Date();
    this.checkoutDate.setDate(new Date().getDate() + 1);
    this.currentPageNumber = 0;

    const roomsearchValueChanges$ = this.roomsearch.valueChanges;

    roomsearchValueChanges$.subscribe(valChange => {
      this.currentCheckInVal = valChange.checkin;
      this.currentCheckOutVal = valChange.checkout;
      this.currentShowParamVal = valChange.showParam;
      this.currentPageSize = valChange.pageSize;
      this.currentSort = {
        property: valChange.sortBy.split(',')[0],
        direction: valChange.sortBy.split(',')[1]
      };
    });

  }

  searchRooms(pageNumber: number, pageSize: number, sort: PaginationPropertySort) {
    this.bookingResultMessage = undefined;
    this.reservedRoomsBuffer = []; // Cleaning the buffer
    this.getAll(pageNumber, pageSize, sort)
      .subscribe(
        roomsPage => {
          const rooms = roomsPage.content;
          if (rooms.length > 0 && rooms[0].resMsg !== 'ok') {
            this.bookingResultMessage = rooms[0].resMsg;
          } else {
            this.roomsPage = roomsPage;
            if (!this.isSelfSetup) {
              this.self = this;
              this.isSelfSetup = true;
            }
          }
        },
        err => {
          console.log(err);
          this.bookingResultMessage = 'Please, make sure you specify check-in and check-out dates correctly.';
        }
      );
  }

  getAll(pageNumber: number, pageSize: number, sort: PaginationPropertySort): Observable<PaginationPage<Room>> {

    let params = new URLSearchParams();
    params.set('size', `${pageSize}`);
    params.set('page', `${pageNumber}`);
    if (sort !== null) {
      params.set('sort', `${sort.property},${sort.direction}`);
    }
    let options = new RequestOptions({
      search: params
    });
    return this.http.get(this.baseUrl + '/room/reservation/v1?checkin=' + this.currentCheckInVal + '&checkout=' + this.currentCheckOutVal + '&showParam=' + this.currentShowParamVal, options)
      .map(this.mapRoom);

  }
  mapRoom(response: Response): PaginationPage<Room> {
    return response.json();
  }

  reserveRoom(value: string) {
    this.reservedRoomsBuffer.push(parseInt(value));
    this.request = new ReserveRoomRequest(value, this.currentCheckInVal, this.currentCheckOutVal);
    this.createReservation(this.request);
  }
  createReservation(body: ReserveRoomRequest) {
    const bodyString = JSON.stringify(body);
    const headers = new Headers({'Content-Type': 'application/json'});
    const option = new RequestOptions({headers: headers});
    const postRequest = this.http.post(this.baseUrl + '/room/reservation/v1', body, option);
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

  showReserveBtn(roomId: number) {
      return !this.reservedRoomsBuffer.includes(roomId);
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
  roomType: string;
  description: string;
  imgURL: string;
  available: boolean;
  resMsg: string;
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
