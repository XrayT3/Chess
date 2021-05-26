package Figures;

public enum Cell {
    A1(0, 7), A2(0, 6), A3(0, 5), A4(0, 4), A5(0, 3), A6(0, 2), A7(0, 1), A8(0, 0),
    B1(1, 7), B2(1, 6), B3(1, 5), B4(1, 4), B5(1, 3), B6(1, 2), B7(1, 1), B8(1, 0),
    C1(2, 7), C2(2, 6), C3(2, 5), C4(2, 4), C5(2, 3), C6(2, 2), C7(2, 1), C8(2, 0),
    D1(3, 7), D2(3, 6), D3(3, 5), D4(3, 4), D5(3, 3), D6(3, 2), D7(3, 1), D8(3, 0),
    E1(4, 7), E2(4, 6), E3(4, 5), E4(4, 4), E5(4, 3), E6(4, 2), E7(4, 1), E8(4, 0),
    F1(5, 7), F2(5, 6), F3(5, 5), F4(5, 4), F5(5, 3), F6(5, 2), F7(5, 1), F8(5, 0),
    G1(6, 7), G2(6, 6), G3(6, 5), G4(6, 4), G5(6, 3), G6(6, 2), G7(6, 1), G8(6, 0),
    H1(7, 7), H2(7, 6), H3(7, 5), H4(7, 4), H5(7, 3), H6(7, 2), H7(7, 1), H8(7, 0),
    A0(-1, -1);

    private final int x;
    private final int y;

    Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Cell fromInt(int x, int y) {
        switch (x) {
            case 0:
                switch (y) {
                    case 0:
                        return A8;
                    case 1:
                        return A7;
                    case 2:
                        return A6;
                    case 3:
                        return A5;
                    case 4:
                        return A4;
                    case 5:
                        return A3;
                    case 6:
                        return A2;
                    case 7:
                        return A1;
                    default:
                        break;
                }
                break;
            case 1:
                switch (y) {
                    case 0:
                        return B8;
                    case 1:
                        return B7;
                    case 2:
                        return B6;
                    case 3:
                        return B5;
                    case 4:
                        return B4;
                    case 5:
                        return B3;
                    case 6:
                        return B2;
                    case 7:
                        return B1;
                    default:
                        break;
                }
                break;
            case 2:
                switch (y) {
                    case 0:
                        return C8;
                    case 1:
                        return C7;
                    case 2:
                        return C6;
                    case 3:
                        return C5;
                    case 4:
                        return C4;
                    case 5:
                        return C3;
                    case 6:
                        return C2;
                    case 7:
                        return C1;
                    default:
                        break;
                }
                break;
            case 3:
                switch (y) {
                    case 0:
                        return D8;
                    case 1:
                        return D7;
                    case 2:
                        return D6;
                    case 3:
                        return D5;
                    case 4:
                        return D4;
                    case 5:
                        return D3;
                    case 6:
                        return D2;
                    case 7:
                        return D1;
                    default:
                        break;
                }
                break;
            case 4:
                switch (y) {
                    case 0:
                        return E8;
                    case 1:
                        return E7;
                    case 2:
                        return E6;
                    case 3:
                        return E5;
                    case 4:
                        return E4;
                    case 5:
                        return E3;
                    case 6:
                        return E2;
                    case 7:
                        return E1;
                    default:
                        break;
                }
                break;
            case 5:
                switch (y) {
                    case 0:
                        return F8;
                    case 1:
                        return F7;
                    case 2:
                        return F6;
                    case 3:
                        return F5;
                    case 4:
                        return F4;
                    case 5:
                        return F3;
                    case 6:
                        return F2;
                    case 7:
                        return F1;
                    default:
                        break;
                }
                break;
            case 6:
                switch (y) {
                    case 0:
                        return G8;
                    case 1:
                        return G7;
                    case 2:
                        return G6;
                    case 3:
                        return G5;
                    case 4:
                        return G4;
                    case 5:
                        return G3;
                    case 6:
                        return G2;
                    case 7:
                        return G1;
                    default:
                        break;
                }
                break;
            case 7:
                switch (y) {
                    case 0:
                        return H8;
                    case 1:
                        return H7;
                    case 2:
                        return H6;
                    case 3:
                        return H5;
                    case 4:
                        return H4;
                    case 5:
                        return H3;
                    case 6:
                        return H2;
                    case 7:
                        return H1;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}