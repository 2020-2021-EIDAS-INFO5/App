import { ISignOrder } from 'app/shared/model/sign-order.model';

export interface ISignaturePlacement {
  id?: number;
  pageNumber?: number;
  coordinateX?: number;
  coordinateY?: number;
  placement?: ISignOrder;
}

export class SignaturePlacement implements ISignaturePlacement {
  constructor(
    public id?: number,
    public pageNumber?: number,
    public coordinateX?: number,
    public coordinateY?: number,
    public placement?: ISignOrder
  ) {}
}
