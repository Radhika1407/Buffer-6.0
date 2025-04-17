//import dao.*;
//import dsa_modules.*;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.Scanner;
//
//public class Main {
//    private CreditScoreBST creditScoreBST;
//    private FraudDetector fraudDetector;
//    private SegmentTree segmentTree;
//    private Graph borrowerLenderGraph;
//
//    private FraudDAO fraudDAO;
//    private BorrowerDAO borrowerDAO;
//    private RepaymentDAO repaymentDAO;
//
//    private int borrowerIdCounter = 1;
//
//    public Main(Connection conn) throws SQLException {
//        this.creditScoreBST = new CreditScoreBST();
//        this.fraudDetector = new FraudDetector();
//        this.borrowerLenderGraph = new Graph();
//
//        this.fraudDAO = new FraudDAO(conn);
//        this.borrowerDAO = new BorrowerDAO();
//        this.repaymentDAO = new RepaymentDAO(conn);
//
//        int[] repayments = repaymentDAO.getAllRepayments();
//        this.segmentTree = new SegmentTree(repayments);
//    }
//
//    // Register a new borrower
//    public void registerBorrower(String name, int creditScore) {
//        try {
//            if (fraudDAO.isFraud(name)) {
//                System.out.println("User is flagged as fraudulent. Cannot register.");
//                return;
//            }
//
//            Borrower b = new Borrower(borrowerIdCounter++, name, creditScore);
//            creditScoreBST.insert(b);
//            borrowerDAO.saveBorrower(b);
//            borrowerLenderGraph.addBorrower(b.getId());
//
//            repaymentDAO.addRepayment(b.getId(), 0); // Initial repayment as 0
//
//            System.out.println("Borrower registered: " + name + " (ID: " + b.getId() + ")");
//        } catch (SQLException e) {
//            System.out.println("Error during borrower registration: " + e.getMessage());
//        }
//    }
//
//    public void updateRepayment(int index, int amount, int repaymentId) {
//        segmentTree.update(index, amount);
//        try {
//            repaymentDAO.updateRepayment(repaymentId, amount);
//        } catch (SQLException e) {
//            System.out.println("Error updating repayment: " + e.getMessage());
//        }
//    }
//
//    public void queryRepayment(int left, int right) {
//        int total = segmentTree.query(left, right);
//        System.out.println("Total repayment from index " + left + " to " + right + ": " + total);
//    }
//
//    public static void main(String[] args) {
//        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lending_db", "root", "password");
//             Scanner scanner = new Scanner(System.in)) {
//
//            Main system = new Main(conn);
//
//            while (true) {
//                System.out.println("\n1. Register Borrower\n2. Update Repayment\n3. Query Repayments\n4. Exit");
//                int choice = scanner.nextInt();
//                scanner.nextLine(); // consume newline
//
//                switch (choice) {
//                    case 1:
//                        System.out.print("Enter name: ");
//                        String name = scanner.nextLine();
//                        System.out.print("Enter credit score: ");
//                        int credit = scanner.nextInt();
//                        system.registerBorrower(name, credit);
//                        break;
//                    case 2:
//                        System.out.print("Enter index in segment tree: ");
//                        int index = scanner.nextInt();
//                        System.out.print("Enter repayment ID in DB: ");
//                        int repId = scanner.nextInt();
//                        System.out.print("Enter new amount: ");
//                        int amount = scanner.nextInt();
//                        system.updateRepayment(index, amount, repId);
//                        break;
//                    case 3:
//                        System.out.print("Enter start index: ");
//                        int l = scanner.nextInt();
//                        System.out.print("Enter end index: ");
//                        int r = scanner.nextInt();
//                        system.queryRepayment(l, r);
//                        break;
//                    case 4:
//                        return;
//                    default:
//                        System.out.println("Invalid choice");
//                }
//            }
//        } catch (SQLException e) {
//            System.out.println("Database connection error: " + e.getMessage());
//        }
//    }
//}
